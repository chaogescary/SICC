package com.pinyougou.manager.controller;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/** 
	* @date 2018年5月30日下午8:25:56
	* @author Sichao
	*
	* @Description: 增加
	* 增加‘大’对象 
	*/ 
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		//获取登录名
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.getGoods().setSellerId(sellerId);//设置商家ID
		try {
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		//校验是否是当前商家的id		
		Goods goods2 = goodsService.findOne(goods.getGoods().getId());
		//获取当前登录的商家ID
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		//如果传递过来的商家ID并不是当前登录的用户的ID,则属于非法操作
		if(!goods2.getGoods().getSellerId().equals(sellerId) ||  !goods.getGoods().getSellerId().equals(sellerId) ){
			return new Result(false, "操作非法");		
		}
		//
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	@Autowired
	private Destination queueSolrDeleteDestination;//用户在索引库中删除记录

	@Autowired
	private Destination topicPageDeleteDestination;//用于删除静态网页的消息

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(final Long [] ids){
		try {
			goodsService.delete(ids);			
			jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {		
				@Override
				public Message createMessage(Session session) throws JMSException {	
					return session.createObjectMessage(ids);
				}
			});
			//删除页面
			jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {		
				@Override
				public Message createMessage(Session session) throws JMSException {	
					return session.createObjectMessage(ids);
				}
			});	
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
				//获取商家ID
				String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
				//添加查询条件 
				goods.setSellerId(sellerId);		
		return goodsService.findPage(goods, page, rows);		
	}
	
	@Autowired
	private Destination queueSolrDestination;//用于发送solr导入的消息
	
	@Autowired
	private Destination topicPageDestination;

	@Autowired
	private JmsTemplate jmsTemplate;

	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids,String status){
		try {
			goodsService.updateStatus(ids, status);
			//按照SPU ID查询 SKU列表(状态为1)		
			if(status.equals("1")){//审核通过
				//静态页生成
				for(final Long goodsId:ids){
					jmsTemplate.send(topicPageDestination, new MessageCreator() {						
						@Override
						public Message createMessage(Session session) throws JMSException {							
							return session.createTextMessage(goodsId+"");
						}
					});
				}				
			}		
			return new Result(true, "修改状态成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改状态失败");
		}
	}
}

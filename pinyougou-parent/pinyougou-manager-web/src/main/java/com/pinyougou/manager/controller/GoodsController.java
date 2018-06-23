package com.pinyougou.manager.controller;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
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
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	/**
	 * @Autowired 和 @Qualifier 结合使用时，自动注入的策略就从 byType 转变成 byName 
	 */
	@Autowired
	@Qualifier("queueSolrCreateDestination")
	private ActiveMQQueue queueSolrCreateDestination;
	
	@Autowired
	@Qualifier("queueSolrDeleteDestination")
	private ActiveMQQueue queueSolrDeleteDestination;
	
	@Autowired
	@Qualifier("topicPageCreateDestination")
	private ActiveMQTopic topicPageCreateDestination;
	
	@Autowired
	@Qualifier("topicPageDeleteDestination")
	private ActiveMQTopic topicPageDeleteDestination;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部分页列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		String seller = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.getGoods().setSellerId(seller); //新增商品设置商家ID
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
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		if(!name.equals(goods.getGoods().getSellerId())){//防止商品不是当前商家的
			return new Result(false,"非法修改商品");
		}
		
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
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(final Long [] ids){
		try {
			goodsService.delete(ids);
			
/*			//删除solr库中的内容
			itemSearchService.removeItems(ids);
			
			//删除生成的页面
			itemPageService.removeItemHtml(ids);*/
			
			//发送删除solr库内容的消息
			jmsTemplate.send(queueSolrDeleteDestination,new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					// TODO Auto-generated method stub
					return session.createObjectMessage(ids);
				}
			});
			
			//发送删除页面消息
			jmsTemplate.send(topicPageDeleteDestination,new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					// TODO Auto-generated method stub
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
		return goodsService.findPage(goods, page, rows);		
	}
	
	/**
	 * 运营商审核状态的修改
	 * @param ids
	 * @param status
	 */
	@RequestMapping("/updateStatus")
	public Result updateStatus(final Long[] ids, String status){
		try {
			goodsService.updateStatus(ids, status);
			
			if("2".equals(status)){
				//itemSearchService.importItems(ids); //导入solr库
				
				// 发送导入solr库的消息
				jmsTemplate.send(queueSolrCreateDestination,new MessageCreator() {
					public Message createMessage(Session session) throws JMSException {
						return session.createObjectMessage(ids);
					}
				});
				
				
				for (int i = 0; i < ids.length; i++) {
					//生成sku商品静态页面
					//itemPageService.createItemHtml(ids[i]);
					final Long id = ids[i];
					jmsTemplate.send(topicPageCreateDestination,new MessageCreator() {
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(id+"");
						}
					});
				}
				
			}
			
			return new Result(true, "审核完成"); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Result(false, "审核失败"); 
		}
	}
	
	/*@RequestMapping("/createItemHtml")
	public Result createItemHtml(Long goodsId){
		boolean isCreateHtml = itemPageService.createItemHtml(goodsId);
		
		if(isCreateHtml){
			return new Result(true,"生成页面成功");
		}else{
			return new Result(false,"生成页面失败");
		}
	}*/
}

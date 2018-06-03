package com.pinyougou.sellergoods.service;
import java.util.List;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojogroup.Goods;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
 public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	 List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	 PageResult findPage(int pageNum,int pageSize);
	
	
//	 void add(TbGoods goods);
	/** 
	* @date 2018年5月30日下午8:21:03
	* @author Sichao
	*
	* @Description: 增加
	* 修改为‘大’对象 
	*/ 
	 void add(Goods goods);
	
	
	/**
	 * 修改
	 */
	 void update(Goods goods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	 /*TbGoods findOne(Long id);*/
	 Goods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	 void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	 PageResult findPage(TbGoods goods, int pageNum,int pageSize);
	
	 /** 
	* @date 2018年6月3日下午3:19:40
	* @author Sichao
	*
	* @Description: 批量修改状态 
	*/ 
	void updateStatus(Long []ids,String status);
	 
}

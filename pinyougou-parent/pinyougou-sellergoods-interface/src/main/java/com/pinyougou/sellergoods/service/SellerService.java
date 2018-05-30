package com.pinyougou.sellergoods.service;
import java.util.List;
import com.pinyougou.pojo.TbSeller;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SellerService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbSeller> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbSeller seller);
	
	
	/**
	 * 修改
	 */
	public void update(TbSeller seller);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	/** 
	* @date 2018年5月29日下午11:14:47
	* @author Sichao
	*
	* @Description: 由于自动生成器生成的接口参数类型不匹配,所以将其从Long类型更改为String类型 
	*/ 
	public TbSeller findOne(String id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbSeller seller, int pageNum,int pageSize);
	
	/** 
	* @date 2018年5月29日下午9:54:30
	* @author Sichao
	*
	* @Description: 更新商家状态审核的方法
	*/ 
	public void updateStatus(String sellerId,String status);
	
}

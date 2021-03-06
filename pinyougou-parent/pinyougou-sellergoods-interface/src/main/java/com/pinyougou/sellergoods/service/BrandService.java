package com.pinyougou.sellergoods.service;
import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbBrand;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
 public interface BrandService {

	/**
	 * 返回全部列表
	 * @return
	 */
	 List<TbBrand> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	 PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	 void add(TbBrand brand);
	
	
	/**
	 * 修改
	 */
	 void update(TbBrand brand);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	 TbBrand findOne(Long id);
	
	
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
	 PageResult findPage(TbBrand brand, int pageNum,int pageSize);
	
	/** 
	* @date 2018年5月29日上午11:52:12
	* @author Sichao
	*
	* @Description: 支持前端Select2展示
	*/ 
	List<Map> selectOptionList();
}

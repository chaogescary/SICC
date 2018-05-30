package com.pinyougou.sellergoods.service;
import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojogroup.Specification;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SpecificationService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbSpecification> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbSpecification specification);
	
	
	/**
	 * 修改
	 */
	public void update(TbSpecification specification);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbSpecification findOne(Long id);
	
	
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
	public PageResult findPage(TbSpecification specification, int pageNum,int pageSize);
	
	/** 
	* @date 2018年5月28日下午7:18:05
	* @author Sichao
	*
	* @Description: 新增的方法 
	*/ 
	public void add(Specification specification);
	
	/** 
	* @date 2018年5月28日下午7:24:42
	* @author Sichao
	*
	* @Description: 通过规格ID查询规格和规格选项 
	*/ 
	public Specification findOneObject(Long id);
	
	/** 
	* @date 2018年5月28日下午7:57:29
	* @author Sichao
	*
	* @Description: TODO 
	*/ 
	public void update(Specification specification);
	
	/** 
	* @date 2018年5月29日上午11:52:46
	* @author Sichao
	*
	* @Description: 支持前端Select2展示 
	*/ 
	List<Map> selectOptionList();
}

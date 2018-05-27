package com.pinyougou.sellergoods.service;

import java.util.List;

import com.pinyougou.pojo.TbBrand;

import entity.PageResults;

public interface BrandService {

	public List<TbBrand> findAll();
	
	public PageResults findPage(int pageNum,int pageSize);
	
	public void add(TbBrand brand);
	
	public void update(TbBrand brand);
	
	public TbBrand findOne(Long id);
	
	public void delete(Long [] ids);
	
	public PageResults findPage(TbBrand brand, int pageNum,int pageSize);
}

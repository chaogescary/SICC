package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbItem;

public interface ItemSearchService {
	//根据传入的map中的keywords进行查询solr库返回list集合
	public List<TbItem> search(Map searchMap);
	
	//根据传入的商品id集合进行一次性导入solr库
	public void importItems(Long[] ids);
	
	//根据传入的商品id集合进行一次性删除solr库
	public void removeItems(Long[] ids);
}

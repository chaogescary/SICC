package com.pinyougou.search.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.search.service.ItemSearchService;

@Service
public class ItemSearchServiceImpl implements ItemSearchService{

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private SolrTemplate solrTemplate;
	
	@Override
	public List<TbItem> search(Map searchMap) {
		// TODO Auto-generated method stub
		String keyStr = (String) searchMap.get("keywords"); //页面输入的关键词
		
		SimpleQuery simpleQuery = new SimpleQuery("*:*");
		
		Criteria criteria = new Criteria("item_keywords");
		criteria = criteria.is(keyStr); //设置criteria的搜索条件
		simpleQuery.addCriteria(criteria);
		
		ScoredPage<TbItem> page = solrTemplate.queryForPage(simpleQuery, TbItem.class);
		
		return page.getContent();
	}

	@Override
	public void importItems(Long[] ids) {
		// TODO Auto-generated method stub
		System.out.println("=======importItems");
		TbItemExample example = new TbItemExample();
		example.createCriteria().andGoodsIdIn(Arrays.asList(ids));
		List<TbItem> itemList = itemMapper.selectByExample(example);
		System.out.println("=========itemSize"+itemList.size());
		System.out.println("======ids"+ids);
		for (int i = 0; i < itemList.size(); i++) {
			System.out.println("solr库新增商品===="+itemList.get(i).getTitle());
		}
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
	}

	@Override
	public void removeItems(Long[] ids) {
		// TODO Auto-generated method stub
		TbItemExample example = new TbItemExample();
		example.createCriteria().andGoodsIdIn(Arrays.asList(ids));
		List<TbItem> itemList = itemMapper.selectByExample(example);
		for (TbItem tbItem : itemList) {
			System.out.println("solr库删除商品===="+tbItem.getTitle());
			solrTemplate.deleteById(tbItem.getId()+"");
		}
		solrTemplate.commit();
	}

	@Override
	public void importList(List<TbItem> list) {
		
	}

	@Override
	public void deleteByGoodsIds(List<Long> asList) {
		// TODO Auto-generated method stub
		
	}
}

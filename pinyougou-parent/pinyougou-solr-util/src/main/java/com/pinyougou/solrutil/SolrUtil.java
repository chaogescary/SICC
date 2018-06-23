package com.pinyougou.solrutil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;

@Component
public class SolrUtil {
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private SolrTemplate solrTemplate;
	
	/**
	 * 导入商品数据
	 */
	public void importItemData(){
		//创建实例模板对象
		TbItemExample example=new TbItemExample();
		//创建规则对象
		Criteria criteria = example.createCriteria();
		//创建子句
		criteria.andStatusEqualTo("1");//已审核
		//执行查询
		List<TbItem> itemList = itemMapper.selectByExample(example);
		System.out.println("===商品列表===");
		for(TbItem item:itemList){
			/**
			 * 由于spec字段为json字符串，需要使用JSON.parseObject转化为对象先
			 */
			Map specMap= JSON.parseObject(item.getSpec());
			
			/**
			 * 此处可返回到/pinyougou-pojo/src/main/java/com/pinyougou/pojo/TbItem.java
			 * 里查看spec字段，是设置为动态域的形式
			 */
			item.setSpecMap(specMap);
			System.out.println(item.getTitle());			
		}		
		
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
		
		System.out.println("===结束===");			
	}	
	
	public static void main(String[] args) {
		/**
		 * 由于本身是jar包，无法通过war包web.xml listener加载的方式加载配置文件，
		 * 所以需要使用ApplicationContext对象进行资源访问，即加载配置文件
		 */
		ApplicationContext context=new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		
		/**
		 * 因为SolrUtil类使用了@Component注解
		 * 所以实际上在上一行已经被spring创建了一个对象了，所以71行的写法是错误的
		 * 应该直接从spring上下文对象，即从ApplicationContext对象中取出
		 */
		SolrUtil solrUtil=  (SolrUtil) context.getBean("solrUtil");
//		SolrUtil solrUtil = new SolrUtil();
		//把数据库数据导入到solr
		solrUtil.importItemData();
	}
}

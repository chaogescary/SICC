package com.pinyougou.page.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service

public class ItemPageServiceImpl implements ItemPageService {

	@Value("${pagedir}")
	private String pagedir;
	
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	@Autowired
	private TbGoodsMapper goodsMapper;
	
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Autowired
	private TbItemMapper itemMapper;
		
	/**
	 * 传入的 goodsId 为需要静态化的SPU的商品的ID
	 */
	@Override
	public boolean genItemHtml(Long goodsId){				
		try {
			Configuration configuration = freeMarkerConfig.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			Map dataModel=new HashMap<>();	
			
			/**
			 * 通过1、2、3步将goods、goodsDesc、itemCat数据封装到Map中
			 */
			//1.加载商品表数据SPU(Standard Product Unit)
			TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goods", goods);			
			//2.加载商品扩展表数据			
			TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goodsDesc", goodsDesc);
			//3.商品分类三级目录，为操作面包屑做准备
			String itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
			String itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
			String itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
			dataModel.put("itemCat1", itemCat1);
			dataModel.put("itemCat2", itemCat2);
			dataModel.put("itemCat3", itemCat3);
			
			
			/**
			 * 获取状态有效的，与上面SPU对应的SKU列表，作为商品详情页的展示信息
			 */
			TbItemExample example=new TbItemExample();
			Criteria criteria = example.createCriteria();
			criteria.andStatusEqualTo("1");//状态为有效
			criteria.andGoodsIdEqualTo(goodsId);//指定SPU ID
			example.setOrderByClause("is_default desc");//setOrderByClause按照状态降序，保证第一个为默认			
			List<TbItem> itemList = itemMapper.selectByExample(example);		
			dataModel.put("itemList", itemList);
			//根据预设头路径与唯一的goodsID进行拼接，获得静态页面输出路径
			Writer out=new FileWriter(pagedir+goodsId+".html");
			//放入map，提取 /pinyougou-page-service/src/main/webapp/ftl 目录下的模板，并执行转化
			template.process(dataModel, out);
			out.close();
			return true;			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteItemHtml(Long[] goodsIds) {
		try {
			for(Long goodsId:goodsIds){
				new File(pagedir+goodsId+".html").delete();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
}

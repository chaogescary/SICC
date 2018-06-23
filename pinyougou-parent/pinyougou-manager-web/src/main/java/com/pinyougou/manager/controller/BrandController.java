package com.pinyougou.manager.controller;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;
import entity.Result;

/**
 *使用@RestController,相当于@Controller和@ResponseBody的结合，返回json数据不需要在方法前面加@ResponseBody 
 *注解了，但使用@RestController这个注解，就不能返回jsp,html页面，视图解析器无法解析jsp,html页面了
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

	/**
	 * 使用@Reference注入，通过dubbo获取服务接口
	 */
	@Reference
	private BrandService brandService;
	
	/**
	 * 返回全部列表(由逆向工程生成的默认方法)
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbBrand> findAll(){			
		return brandService.findAll();
	}
	
	
	/**
	 * 返回全部列表，实用方法
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return brandService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param brand
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbBrand brand){
		try {
			brandService.add(brand);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param brand
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbBrand brand){
		try {
			brandService.update(brand);
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
	public TbBrand findOne(Long id){
		return brandService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			brandService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
	/**
	 * 查询+分页
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbBrand brand, int page, int rows  ){
		return brandService.findPage(brand, page, rows);		
	}
	
	/** 
	* @Description: 支持前端Select2展示,返回List<Map> 其中,Map为brand表的字段 <id,name> 
	*/ 
	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return brandService.selectOptionList();
	}

	
}

package com.sicc;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerProccessor {

	public static void main(String[] args) {
		FileWriter out = null;
		try {
			// 创建包含版本信息的配置类对象
			Configuration configuration = new Configuration(Configuration.getVersion());
			// 获取到模板文件对象
			configuration.setDirectoryForTemplateLoading(new File(configuration.getClass().getResource("/").getPath()));
			// 设置字符集编码
			configuration.setDefaultEncoding("utf-8");
			// 获取到模板对象
			Template template = configuration.getTemplate("test.ftl");
			// 构建对象
			HashMap map = new HashMap();
			map.put("name", "张三");
			map.put("message", "welcome");
			map.put("success", true);
			// 构建List测试对象
			List goodsList = new ArrayList();
			Map goods1 = new HashMap();
			goods1.put("name", "苹果");
			goods1.put("price", 5.8);
			Map goods2 = new HashMap();
			goods2.put("name", "香蕉");
			goods2.put("price", 2.5);
			Map goods3 = new HashMap();
			goods3.put("name", "橘子");
			goods3.put("price", 3.2);
			goodsList.add(goods1);
			goodsList.add(goods2);
			goodsList.add(goods3);
			map.put("goodsList", goodsList);
			// 输出页面
			out = new FileWriter(new File("C:\\Users\\Sichao\\Desktop\\final.html"));
			// 执行转换
			template.process(map, out);
			System.out.println("转换成功");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("转换失败");
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					System.out.println("流关闭失败");
				}
			}
		}
	}
}

package com.sicc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerProccessor {

	public static void main(String[] args) throws IOException, TemplateException {
		Configuration configuration = new Configuration(Configuration.getVersion());
		configuration.setDirectoryForTemplateLoading(new File("C:\\Users\\Sichao\\Desktop"));
		configuration.setDefaultEncoding("utf-8");
		Template template = configuration.getTemplate("test.ftl");
		HashMap map = new HashMap();
		map.put("name", "张三");
		map.put("message", "welcome");
		FileWriter out = new FileWriter(new File("C:\\Users\\Sichao\\Desktop\\final.html"));
		template.process(map, out);
		out.close();
	}

}

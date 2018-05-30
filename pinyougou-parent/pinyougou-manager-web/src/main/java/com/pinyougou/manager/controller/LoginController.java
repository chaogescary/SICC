package com.pinyougou.manager.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
	/** 
	* @date 2018年5月29日下午8:11:02
	* @author Sichao
	*
	* @Description: 主界面显示登陆用户名 
	*/ 
	@RequestMapping("/name")
	public Map name() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap();
		map.put("loginName", name);
		return map;
	}
}

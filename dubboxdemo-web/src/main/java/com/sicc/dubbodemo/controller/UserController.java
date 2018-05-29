package com.sicc.dubbodemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sicc.dubbodemo.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Reference
	private UserService userService;
	@RequestMapping("/showName")
	public String ShowName() {
		return userService.getName();
	}
}

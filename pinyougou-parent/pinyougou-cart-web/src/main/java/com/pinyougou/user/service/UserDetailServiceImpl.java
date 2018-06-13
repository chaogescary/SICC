package com.pinyougou.user.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//注意认证类的包名要和spring-security.xml文件中的包名一致
//该类可以拷贝shop-web子模块中的
//该类的目的已经不是为了进行用户认证，在调用该类前springSecurity已经从CAS做完认证后才调用，该类的执行只为了拿到角色"ROLE_USER"返回用户角色信息
public class UserDetailServiceImpl implements UserDetailsService {

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("经过认证类:"+username);
		
		List<GrantedAuthority> authorities=new ArrayList();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		return new User(username,"",authorities);
	}
}

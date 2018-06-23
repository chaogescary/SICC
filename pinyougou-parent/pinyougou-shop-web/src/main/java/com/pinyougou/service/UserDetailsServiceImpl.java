package com.pinyougou.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;

/**
 * 认证提供类
 * authentication-manager将会把表单提交的username作为实参传入到loadUserByUsername方法中
 * 如果不存在该商家，则返回null，
 * 如果存在该商家，但是商家状态值不是审核通过，也返回null
 * 否则对商家授权，放行
 */
public class UserDetailsServiceImpl implements UserDetailsService {

	private SellerService sellerService;
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//构建权限列表
		List<GrantedAuthority> grantAuths=new ArrayList();
		//添加权限角色
		grantAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));
		//得到商家对象
		TbSeller seller = sellerService.findOne(username);
		if(seller!=null){
			if(seller.getStatus().equals("1")){
				return new User(username,seller.getPassword(),grantAuths);
			}else{
				return null;
			}			
		}else{
			return null;
		}
	}
}

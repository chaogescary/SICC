package com.pinyougou.cart.service;

import java.util.List;

import entity.Cart;

	public interface CartService {
		/**
		 * 添加商品到购物车
		 * @param cartList    购物车
		 * @param itemId      sku
		 * @param num	            数量
		 * @return
		 */
		List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num );
		
		/**
		 * 从redis中查询购物车
		 * @param username    通过用户为主键进行查询查询
		 * @return
		 */
		List<Cart> findCartListFromRedis(String username);
		
		/**
		 * 将购物车保存到redis
		 * @param username    通过用户为主键进行查询查询
		 * @param cartList    购物车
		 */
		void saveCartListToRedis(String username,List<Cart> cartList);
		
		/**
		 * 合并购物车
		 * @param cartList1
		 * @param cartList2
		 * @return
		 */
		List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);
		
	}

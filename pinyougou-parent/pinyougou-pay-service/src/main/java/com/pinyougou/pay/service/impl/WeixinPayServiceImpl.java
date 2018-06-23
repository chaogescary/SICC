package com.pinyougou.pay.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.pay.service.WeixinPayService;

import util.HttpClient;

@Service
public class WeixinPayServiceImpl implements WeixinPayService{

	@Value("${appid}")
	private String appid;
	
	@Value("${partner}")
	private String partner; //商户号
	
	@Value("${notifyurl}")
	private String notifyurl;
	
	@Value("${partnerkey}")
	private String partnerkey;
	
	@Override
	public Map createNative(String out_trade_no, String total_fee) {
		HashMap map = new HashMap();
		
		// 准备请求参数
		HashMap requestMap = new HashMap();
		requestMap.put("appid", appid);
		requestMap.put("mch_id", partner);
		requestMap.put("nonce_str", WXPayUtil.generateNonceStr());
		requestMap.put("body", "品优购电商项目");
		requestMap.put("out_trade_no", out_trade_no);
		requestMap.put("total_fee", total_fee);
		requestMap.put("spbill_create_ip", "127.0.0.1");
		requestMap.put("notify_url", notifyurl);
		requestMap.put("trade_type", "NATIVE");
		
		try {
			//通过微信工具类将map转成xml格式
			String requestStr = WXPayUtil.generateSignedXml(requestMap, partnerkey);
			
			System.out.println("生成订单的请求==="+requestStr);
			
			// 发送http请求
			HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
			httpClient.setXmlParam(requestStr);
			httpClient.setHttps(true);
			httpClient.post();
			String content = httpClient.getContent();
			
			Map<String, String> responseMap = WXPayUtil.xmlToMap(content);
			
			System.out.println("微信返回的==="+content);
			
			map.put("out_trade_no", out_trade_no);
			map.put("total_fee", total_fee);
			map.put("code_url", responseMap.get("code_url"));
			
			return map;
			// 封装返回结果
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map queryPayStatus(String out_trade_no) {
		// TODO Auto-generated method stub
		// 1.封装请求参数
		HashMap requestMap = new HashMap();
		requestMap.put("appid", appid);
		requestMap.put("mch_id", partner);
		requestMap.put("out_trade_no", out_trade_no);
		requestMap.put("nonce_str", WXPayUtil.generateNonceStr());
		// 2.通过工具类转成xml格式的string字符串
		try {
			String requestStr = WXPayUtil.generateSignedXml(requestMap, partnerkey);
			System.out.println("向微信发送的====="+requestStr);
			// 3.通过httpclient工具类进行发送，设置请求方式，设置请求内容，发送请求，获取内容
			HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
			httpClient.setHttps(true);
			httpClient.setXmlParam(requestStr);
			httpClient.post();
			String content = httpClient.getContent();
			System.out.println("微信返回的====="+content);
			return WXPayUtil.xmlToMap(content); //直接将微信返回的内容向controller返回
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

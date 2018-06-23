package com.pinyougou.cart.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;

import entity.Result;
import util.IdWorker;

@RestController
@RequestMapping("/pay")
public class PayController {

	@Reference
	private WeixinPayService  weixinPayService;
	
	@Reference
	private OrderService orderService;
	
	/**
	 * 生成二维码的方法
	 * @return
	 */
	@RequestMapping("/createNative")
	public Map createNative(){
		//long nextId = new IdWorker().nextId();
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		
		TbPayLog payLog = orderService.searchPayLogFromRedis(userId);
		
		if(payLog == null){
			return new HashMap();
		}
		return weixinPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee() + "");
	}
	
	@RequestMapping("/queryPayStatus")
	public Result queryPayStatus(String out_trade_no) {
		int time = 1;
		
		while(true){
			Map responseMap = weixinPayService.queryPayStatus(out_trade_no);
			
			//设置5秒请求一次订单状态
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//微信返回的内容成null
			if(responseMap == null){
				return new Result(false,"付款失败");
			}
			
			//如果支付状态是成功的话
			if("SUCCESS".equals(responseMap.get("trade_state"))){
				return new Result(true,"付款成功");
			}
			
			//判断超过30秒终止查询
			if(time > 6){
				return new Result(false,"timeout");
			}
			time++;
		}
	}
}

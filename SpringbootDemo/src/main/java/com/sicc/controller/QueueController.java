package com.sicc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueueController {
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@RequestMapping("/send")
	public void send(String text){
		jmsMessagingTemplate.convertAndSend("itcast", text);
	}
	
	@RequestMapping("/sendmap")
	public void sendMap(){
		Map map=new HashMap<>();
		map.put("mobile", "17600388002");
		map.put("content", "恭喜获得10元代金券");		
		jmsMessagingTemplate.convertAndSend("itcast_map",map);
	}
	
	@RequestMapping("/sendsms")
	public void sendSms(){
		Map map=new HashMap<>();
		map.put("mobile", "17600388002");
		map.put("template_code", "SMS_137380040");	
		map.put("sign_name", "品优购验证");
//		map.put("param", "{\"${code}\":\"102931\"}");
		map.put("param","{\"${code}\":\"Tom\", \"code\":\"mofaniloveyou\"}");
		jmsMessagingTemplate.convertAndSend("sms",map);
	}
}
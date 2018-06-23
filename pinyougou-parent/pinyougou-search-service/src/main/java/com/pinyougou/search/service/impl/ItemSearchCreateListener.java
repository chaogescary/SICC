package com.pinyougou.search.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.search.service.ItemSearchService;

@Component
public class ItemSearchCreateListener implements MessageListener{

	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {
		ObjectMessage oMessage = (ObjectMessage) message;
		try {
			Long[] ids = (Long[]) oMessage.getObject();
			System.out.println("===收到创建solr库消息==="+ids);
			
			itemSearchService.importItems(ids);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}

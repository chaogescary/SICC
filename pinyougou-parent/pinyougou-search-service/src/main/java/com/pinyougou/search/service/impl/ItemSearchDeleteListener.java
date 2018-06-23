package com.pinyougou.search.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.search.service.ItemSearchService;

@Component
public class ItemSearchDeleteListener implements MessageListener{

	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		ObjectMessage oMessage = (ObjectMessage) message;
		try {
			Long[] ids = (Long[]) oMessage.getObject();
			System.out.println("===收到删除solr库消息==="+ids);
			
			itemSearchService.removeItems(ids);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
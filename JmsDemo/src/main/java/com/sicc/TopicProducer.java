package com.sicc;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicProducer {

	public static void main(String[] args) throws JMSException {

		//创建连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.141:61616");
		//获取连接
		Connection connection = connectionFactory.createConnection();
		//获取session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//获取主题
		Topic topic = session.createTopic("test-topic");
		//获取生产者
		MessageProducer producer = session.createProducer(topic);
		//获取消息
		TextMessage textMessage = session.createTextMessage("goufan");
		//发送消息
		producer.send(textMessage);
		//关闭资源
		producer.close();
		session.close();
		connection.close();
		
	}

}

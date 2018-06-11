package com.sicc;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class QueueProducer {

	public static void main(String[] args) throws JMSException {
		
		//创建连接工厂
		ConnectionFactory connectionFactory  = new ActiveMQConnectionFactory("tcp://192.168.25.141:61616");
		//获取连接
		Connection connection = connectionFactory.createConnection();
		//启动连接
		connection.start();
		//获取Session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建队列对象
		Queue queue = session.createQueue("test-queue");
		//创建消息生产者
		MessageProducer producer = session.createProducer(queue);
		//创建消息
		TextMessage message = session.createTextMessage("fanfan");
		//发送消息
		producer.send(message);
		//关闭资源
		producer.close();
		session.close();
		connection.close();
	}

}

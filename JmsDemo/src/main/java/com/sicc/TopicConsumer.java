package com.sicc;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicConsumer {

	public static void main(String[] args) throws JMSException, IOException {

		//新建连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.141:61616");
		//创建连接
		Connection connection = connectionFactory.createConnection();
		//创建session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建topic
		Topic topic = session.createTopic("test-topic");
		//创建消费者
		MessageConsumer consumer = session.createConsumer(topic);
		
		//监听消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage)message;
				try {
					System.out.println("接收到的消息:"+textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//等待键盘输入
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}

}

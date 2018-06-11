package com.sicc;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class QueueConsumer {

	public static void main(String[] args) throws JMSException, IOException {
		//创建连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.141:61616");
		//获取连接
		Connection connection = connectionFactory.createConnection();
		//启动链接
		connection.start();
		//获取session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//获取队列对象
		Queue queue = session.createQueue("test-queue");
		//获取消费者
		MessageConsumer consumer = session.createConsumer(queue);
		
		//监听消息
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage)message;
				try {
					System.out.println("接收到消息:"+textMessage.getText());
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

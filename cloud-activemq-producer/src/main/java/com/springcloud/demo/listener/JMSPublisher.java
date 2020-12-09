package com.springcloud.demo.listener;

import com.springcloud.demo.factory.JmsTemplateFactory;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class JMSPublisher {

    /**
 　　* 发送消息
 　　* Topic 生产者
 　　* @param dest 目的地
 　　* @param msg 消息内容
 　　*/
    public static void sendTopicMessage(String dest, String msg) {
        JmsTemplateFactory.getInstance().getTopicJmsTemplate().send(dest, new MessageCreator(){
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });
    }

    /**
 　　* 发送消息
 　　* Queue 生产者
 　　* @param dest 目的地
 　　* @param msg 消息内容
 　　*/
    public static void sendQueueMessage(String dest, String msg) {
        JmsTemplateFactory.getInstance().getQueueJmsTemplate().send(dest, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });
    }
}

package com.springcloud.demo.listener;

import com.springcloud.demo.factory.ConnectionActivemqFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SimpleMessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JMS监听器  创建消费者
 */
public class JMSListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JMSListener.class);
    private static final Map<String, Destination> MQDESTS = new ConcurrentHashMap();

    /**
 　　* 开启一个 点对点的 消息队列监听 的消费者
 　　* @param queueName 队列名称
 　　* @param subName 订阅者的名字
 　　* @param listener 监听
 　　*/

     public static synchronized void startJmsQueueListener(String queueName, MessageListener listener){
        startJmsQueueListener(queueName, null, listener);
    }

    public static synchronized void startJmsQueueListener(String queueName, String subName, MessageListener listener) {
        Destination dst = (Destination)MQDESTS.get("QUEUE_" + queueName);
        if (dst == null) {
            ActiveMQQueue mq = new ActiveMQQueue(queueName);
            startJmsListener(mq, subName, listener);
            MQDESTS.put("QUEUE_" + queueName, mq);
        } else {
            LOGGER.warn(queueName + " already started");
        }
    }

    /**
 　　* 开启 一对多 主题的 消息监听的消费者
 　　*
 　　* @param topicName 主题消息名称
 　　* @param subName 订阅者的名字
 　　* @param listener 监听
 　　*/
    public static synchronized void startJmsTopicListener(String topicName, MessageListener listener){
        startJmsTopicListener(topicName, null, listener);
    }

    public static synchronized void startJmsTopicListener(String topicName, String subName, MessageListener listener) {
        ActiveMQTopic mq = new ActiveMQTopic(topicName);
        startJmsListener(mq, subName, listener);
        MQDESTS.put("QUEUE_" + topicName, mq);
    }


    /**
 　　* 开始 消息监听器 消费者
 　　*
 　　* @param dest 目的地
 　　* @param subName 持久订阅的名字
 　　* @param msgListener 消息监听器
 　　*/
    private static void startJmsListener(Destination dest, String subName, MessageListener msgListener){
        ConnectionFactory factory = ConnectionActivemqFactory.getInstance();

        SimpleMessageListenerContainer listener = new SimpleMessageListenerContainer();
        listener.setConnectionFactory(factory);
        listener.setDestination(dest);
        listener.setMessageListener(msgListener);
        if ((subName != null) && (subName != "")) {
            listener.setDurableSubscriptionName(subName);
        }
        listener.start();
    }
}

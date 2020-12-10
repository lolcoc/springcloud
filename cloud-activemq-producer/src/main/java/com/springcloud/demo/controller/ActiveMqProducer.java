package com.springcloud.demo.controller;

import com.springcloud.demo.listener.JMSListener;
import com.springcloud.demo.listener.JMSPublisher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/active")
public class ActiveMqProducer{
    /**
     * 生产者 发布消息
     * @throws
     */
    @RequestMapping("/start")
    @ResponseBody
    public void testSendMessage() throws InterruptedException {
        for (int idx = 1; idx < 1000000; idx++) {

            //生产者 发布 消息到 queue/queue_b 的队列中
            JMSPublisher.sendQueueMessage("queue/queue_b", String.valueOf(idx));

            // 生产者 发布消息 到  topic/send 的Topic 主题中
            //JMSPublisher.sendTopicMessage("topic/send", String.valueOf(idx * 1111));
        }
    }


    /**
     * 消费者 订阅接受消息
     */
    @RequestMapping("/stop")
    @ResponseBody
    public void receiver() {

        //消费者 订阅主题  topic/send 是否有消息发布，有侧打印出来  （通过 onMessage 监听）
        /*JMSListener.startJmsTopicListener("topic/send", new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage msg = (TextMessage) message;
                        System.out.println("== 收到一个JMS消息..." + msg.getText());
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });*/

        //消费者 订阅队列  queue/queue_b 是否有消息发布，有侧打印出来  （通过 onMessage 监听）
        List list = new ArrayList();
        JMSListener.startJmsQueueListener("queue/queue_b" ,new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage msg = (TextMessage) message;
                        String text = msg.getText();
                        list.add(text);
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            System.out.println(list.size());
            System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(ActiveMqProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @ResponseBody
    @RequestMapping("/test")
    public String testMessege(){
        return "success";
    }

}

package com.springcloud.demo.factory;

import org.springframework.jms.core.JmsTemplate;

public class JmsTemplateFactory {

    private final javax.jms.ConnectionFactory factory;
    private JmsTemplate topicJmsTemplate;
    private JmsTemplate queueJmsTemplate;
    private static JmsTemplateFactory INSTANCE = new JmsTemplateFactory();

    public static JmsTemplateFactory getInstance(){
        return INSTANCE;
    }

    private JmsTemplateFactory(){
        this.factory = ConnectionActivemqFactory.getInstance();
    }

    public synchronized JmsTemplate getTopicJmsTemplate() {
        if (this.topicJmsTemplate == null) {
            this.topicJmsTemplate = createTemplate(this.factory, true);
        }
        return this.topicJmsTemplate;
    }

    public synchronized JmsTemplate getQueueJmsTemplate() {
        if (this.queueJmsTemplate == null) {
            this.queueJmsTemplate = createTemplate(this.factory, false);
        }
        return this.queueJmsTemplate;
    }

    private JmsTemplate createTemplate(javax.jms.ConnectionFactory factory, boolean pubSubDomain) {
        JmsTemplate template = new JmsTemplate(factory);
        template.setPubSubDomain(pubSubDomain);
        return template;
    }
}

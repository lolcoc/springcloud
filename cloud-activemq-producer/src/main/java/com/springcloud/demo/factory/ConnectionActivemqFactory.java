package com.springcloud.demo.factory;

import com.springcloud.demo.config.AMQConfigBean;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.jms.connection.CachingConnectionFactory;

import javax.jms.ConnectionFactory;

public class ConnectionActivemqFactory {
    private static final String URL = "tcp://172.16.21.156:61616";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private static final int SESSIONCACHESIZE = 20;
    private ConnectionFactory factory;

    public static synchronized ConnectionFactory getInstance()
    {
        if (SingletonHolder.INSTANCE.factory == null) {
            SingletonHolder.INSTANCE.build();
        }
        return SingletonHolder.INSTANCE.factory;
    }

    private void build()
    {
        AMQConfigBean bean = loadConfigure();
        this.factory = buildConnectionFactory(bean);
    }

    private ConnectionFactory buildConnectionFactory(AMQConfigBean bean) {
        ConnectionFactory targetFactory = new ActiveMQConnectionFactory(bean.getUserName(), bean.getPassword(), bean.getBrokerURL());

        CachingConnectionFactory connectoryFacotry = new CachingConnectionFactory();
        connectoryFacotry.setTargetConnectionFactory(targetFactory);
        connectoryFacotry.setSessionCacheSize(bean.getSessionCacheSize());

        return connectoryFacotry;
    }

    private AMQConfigBean loadConfigure() {
        if ( URL != null) {
            try {
                return new AMQConfigBean(URL, USERNAME, PASSWORD, SESSIONCACHESIZE);
            } catch (Exception e) {
                throw new IllegalStateException("load amq config error!");
            }
        }
        throw new IllegalStateException("load amq config error!");
    }

    private static class SingletonHolder {
        static ConnectionActivemqFactory INSTANCE = new ConnectionActivemqFactory();
    }
}
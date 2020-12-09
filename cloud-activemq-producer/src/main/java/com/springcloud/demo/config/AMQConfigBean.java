package com.springcloud.demo.config;

public class AMQConfigBean {
    private String brokerURL;
    private String userName;
    private String password;
    private int sessionCacheSize;

    public AMQConfigBean() {
    }

    public AMQConfigBean(String brokerURL, String userName, String password, int sessionCacheSize) {
        this.brokerURL = brokerURL;
        this.userName = userName;
        this.password = password;
        this.sessionCacheSize = sessionCacheSize;
    }

    public String getBrokerURL() {
        return this.brokerURL;
    }

    public void setBrokerURL(String brokerURL) {
        this.brokerURL = brokerURL;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSessionCacheSize() {
        return this.sessionCacheSize;
    }

    public void setSessionCacheSize(int sessionCacheSize) {
        this.sessionCacheSize = sessionCacheSize;
    }
}

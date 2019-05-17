package com.communicator490.communication;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class Message {
    protected String content;
    protected InetAddress ip;
    protected int port;


    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getContent() {
        return content;
    }
}

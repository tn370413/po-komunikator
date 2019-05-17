package com.communicator490.communication;

import java.net.InetAddress;

// class that represents a single message
// it is abstract since we need to differentiate between various types of messages
// sent message =/= received message =/= meta-message from communicator to user

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

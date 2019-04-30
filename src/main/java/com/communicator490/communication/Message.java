package com.communicator490.communication;

import java.net.InetAddress;

public class Message {
    private String content;
    private InetAddress fromIP;
    private int fromPort;

    public Message(String content, InetAddress fromIP, int fromPort) {
        this.content = content;
        this.fromIP = fromIP;
        this.fromPort = fromPort;
    }

    public InetAddress getFromIP() {
        return fromIP;
    }

    public String getContent() {
        return content;
    }
}

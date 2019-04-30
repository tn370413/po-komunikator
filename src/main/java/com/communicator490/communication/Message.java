package com.communicator490.communication;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Message {
    private String content;
    private InetAddress ip;
    private int port;

    public Message(String content, InetAddress ip, int port) {
        this.content = content;
        this.ip = ip;
        this.port = port;
    }

    public Message(String content, String ip, int port) {
        this.content = content;
        try {
            this.ip = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace(); // TODO
        }
        this.port = port;
    }

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

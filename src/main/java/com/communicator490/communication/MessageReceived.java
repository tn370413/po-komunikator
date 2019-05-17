package com.communicator490.communication;

import java.net.InetAddress;

public class MessageReceived extends Message {
    public MessageReceived(String content, InetAddress ip, int port) {
        this.content = content;
        this.ip = ip;
        this.port = port;
    }
}

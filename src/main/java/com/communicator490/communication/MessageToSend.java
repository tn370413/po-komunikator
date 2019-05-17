package com.communicator490.communication;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MessageToSend extends Message {

    public MessageToSend(String content, InetAddress ip, int port) {
        this.content = content;
        this.ip = ip;
        this.port = port;
    }

    public MessageToSend(String content, String ip, int port) throws UnknownHostException {
        this.content = content;
        this.ip = InetAddress.getByName(ip);
        this.port = port;
    }

}

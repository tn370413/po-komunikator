package com.communicator490.communication;

import java.net.InetAddress;

public class Conversation {
    private InetAddress foreignIP;

    public Conversation(InetAddress foreignIP) {
        this.foreignIP = foreignIP;
    }

    public void receiveMessage(Message m) {
    }

    public String getForeignAddress() {
        return foreignIP.getHostAddress();
    }
}

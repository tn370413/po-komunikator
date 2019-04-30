package com.communicator490.communication;

import java.net.InetAddress;

public class Conversation {
    private InetAddress foreignIP;
    private int foreignPort;

    public Conversation(InetAddress foreignIP, int foreignPort) {
        this.foreignIP = foreignIP;
        this.foreignPort = foreignPort;
    }

    public String getForeignAddress() {
        return foreignIP.getHostAddress();
    }

    public int getForeignPort() {
        return foreignPort;
    }
}

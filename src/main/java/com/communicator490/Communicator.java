package com.communicator490;

import com.communicator490.communication.Conversation;
import com.communicator490.communication.Message;
import com.communicator490.communication.Server;
import com.communicator490.controllers.conversationWindow.ConversationWindowController;
import com.communicator490.controllers.mainWindow.MainWindowController;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Communicator {
    private Server server;
    private MainWindowController mainWindowController;
    private HashMap<String, ConversationWindowController> conversationWindowControllers = new HashMap<>();

    private static Communicator communicator = new Communicator();

    public static Communicator getInstance() {
        return communicator;
    }

    private Communicator() {
        try {
            this.server = new Server(); // TODO handle exception
        } catch (SocketException e) {
            System.out.println("FUCK");
        }
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public int getPort() {
        return -1; // TODO
    }

    public int getInternalPort() {
        return server.getInternalPort();
    }

    public String getIp() {
        return "???"; // TODO
    }

    public String getInternalIp() {
        String ip = "???";

        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public void stop() {
        server.stop();
    }

    public void receiveMessage(Message m) {
        InetAddress fromIP = m.getFromIP();
        if (!conversationWindowControllers.containsKey(fromIP.getHostAddress())) {
            Conversation conversation = new Conversation(fromIP);
            ConversationWindowController conversationWindowController = mainWindowController.openConversationWindow(conversation);
            conversationWindowControllers.put(fromIP.getHostAddress(), conversationWindowController);
        }
        conversationWindowControllers.get(fromIP.getHostAddress()).receiveMessage(m.getContent());
    }

    public void endConversation(ConversationWindowController windowController) {
        conversationWindowControllers.remove(windowController.getConversation().getForeignAddress());
    }
}

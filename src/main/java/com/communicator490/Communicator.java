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

    public void openConversation(String ip, int port) {
        if (!conversationWindowControllers.containsKey(ip)) {
            Conversation conversation = null;
            try {
                conversation = new Conversation(InetAddress.getByName(ip), port);
            } catch (UnknownHostException e) {
                e.printStackTrace(); // TODO
            }
            ConversationWindowController conversationWindowController = mainWindowController.openConversationWindow(conversation);
            conversationWindowControllers.put(ip, conversationWindowController);
        } else {
            java.awt.Toolkit.getDefaultToolkit().beep();
            // notify user & show conversation window
        }
    }

    public void receiveMessage(Message m) {
        InetAddress fromIP = m.getIp();
        openConversation(fromIP.getHostAddress(), m.getPort());
        conversationWindowControllers.get(fromIP.getHostAddress()).receiveMessage(m.getContent());
    }

    public void endConversation(ConversationWindowController windowController) {
        conversationWindowControllers.remove(windowController.getConversation().getForeignAddress());
    }

    public void sendMessage(Message message) {
        server.sendMessage(message);
    }
}

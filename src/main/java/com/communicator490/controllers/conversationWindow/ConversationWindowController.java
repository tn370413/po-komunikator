package com.communicator490.controllers.conversationWindow;

import com.communicator490.Communicator;
import com.communicator490.communication.Conversation;
import com.communicator490.communication.Message;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ConversationWindowController {
    @FXML
    private Label headerLabel;

    @FXML
    private MessagesOuterBoxController messagesOuterBoxController;

    @FXML
    private TextField messageToSend;

    @FXML
    private TextField messageToSendPortDebug;

    @FXML
    private Button sendButton;

    private Stage stage;
    private Conversation conversation;

    private void sendMessage(Message message) {
        Communicator.getInstance().sendMessage(message);
        messagesOuterBoxController.displayNewMessage(message.getContent(), true);
    }

    public void initialize() {
        sendButton.setOnMouseClicked(mouseEvent -> {
            Message message = new Message(messageToSend.getText(), "127.0.0.1",
                    Integer.parseInt(messageToSendPortDebug.getText()));
            sendMessage(message);
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Conversation");
        stage.setOnCloseRequest(closeWindowHandler);
    }

    private EventHandler<WindowEvent> closeWindowHandler = windowEvent -> Communicator.getInstance().endConversation(this);

    public void receiveMessage(String content) {
        messagesOuterBoxController.displayNewMessage(content, false);
        stage.sizeToScene();
    }

    public void setConversation(Conversation conversation) {
        headerLabel.setText("Conversation with " + conversation.getForeignAddress() + String.format(" (port %d)", conversation.getForeignPort()));
        this.conversation = conversation;
    }

    public Conversation getConversation() {
        return conversation;
    }
}

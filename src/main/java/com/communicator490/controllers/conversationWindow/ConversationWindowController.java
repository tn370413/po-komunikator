package com.communicator490.controllers.conversationWindow;

import com.communicator490.Communicator;
import com.communicator490.communication.Conversation;
import com.communicator490.communication.Message;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private Button sendButton;

    private Stage stage;
    private Conversation conversation;

    private void sendMessage(Message message) {
        Communicator.getInstance().sendMessage(message);
        messagesOuterBoxController.displayNewMessage(message.getContent(), true);
    }

    public void initialize() {
        sendButton.setOnAction(actionEvent -> {
            Message message = new Message(messageToSend.getText(), conversation.getForeignAddress(), conversation.getForeignPort());
            sendMessage(message);
            messageToSend.setText("");
        });

        messageToSend.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                sendButton.fire();
                keyEvent.consume();
            }
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

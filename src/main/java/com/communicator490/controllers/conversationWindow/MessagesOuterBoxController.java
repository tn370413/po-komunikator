package com.communicator490.controllers.conversationWindow;

import com.communicator490.customControls.MessageControl;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MessagesOuterBoxController {
    @FXML
    private VBox messagesBox;

    @FXML
    private ScrollPane messagesBoxScrollPane;

    public void displayNewMessage(String content, boolean fromMe) {
        MessageControl messageControl = new MessageControl(content);
        if (fromMe) {
            messageControl.getStyleClass().add("sent-message");
        } else {
            messageControl.getStyleClass().add("received-message");
        }
        messageControl.getController().bindWidth(messagesBoxScrollPane.widthProperty());
        messagesBox.getChildren().add(messageControl);
        messagesBoxScrollPane.setVvalue(1.0);
    }
}

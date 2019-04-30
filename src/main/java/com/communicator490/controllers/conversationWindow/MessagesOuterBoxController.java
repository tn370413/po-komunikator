package com.communicator490.controllers.conversationWindow;

import com.communicator490.customControls.MessageControl;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class MessagesOuterBoxController {
    @FXML
    private VBox messagesBox;

    public void displayNewMessage(String content, boolean fromMe) {
        MessageControl messageControl = new MessageControl(content);
        if (fromMe) {
            messageControl.getStyleClass().add("sent-message");
        } else {
            messageControl.getStyleClass().add("received-message");
        }
        messagesBox.getChildren().add(messageControl);
    }
}

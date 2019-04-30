package com.communicator490.controllers.conversationWindow;

import com.communicator490.customControls.MessageControl;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class MessagesOuterBoxController {
    @FXML
    private VBox messagesBox;


    public void receiveMessage(String content) {
        MessageControl messageControl = new MessageControl(content);
        messagesBox.getChildren().add(messageControl);
    }
}

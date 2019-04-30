package com.communicator490.customControls;

import com.communicator490.controllers.conversationWindow.MessageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MessageControl extends VBox {
    public MessageControl() {
        this("HELLO YOU FUCKS");
    }

    public MessageControl(String content) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/communicator490/fxml/conversationWindow/messageControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(new MessageController(content));

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}

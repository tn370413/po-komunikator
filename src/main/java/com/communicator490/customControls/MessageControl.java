package com.communicator490.customControls;

import com.communicator490.controllers.conversationWindow.MessageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MessageControl extends VBox {
    private MessageController controller;

    public MessageControl(String content) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/communicator490/fxml/conversationWindow/messageControl.fxml"));
        fxmlLoader.setRoot(this);
        controller = new MessageController(content);
        fxmlLoader.setController(controller);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public MessageController getController() {
        return controller;
    }
}

package com.communicator490.customControls;

import com.communicator490.controllers.conversationWindow.MessageController;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

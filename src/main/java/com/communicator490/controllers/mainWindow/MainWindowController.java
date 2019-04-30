package com.communicator490.controllers.mainWindow;

import com.communicator490.communication.Conversation;
import com.communicator490.controllers.conversationWindow.ConversationWindowController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainWindowController extends MainWindowComponentController {

    @FXML
    private YourIdInfoController yourIdInfoControlController;

    @FXML
    private TextField theirIp;

    @FXML
    private TextField theirPort;

    @FXML
    private Button connectButton;

    public void setStage(Stage stage) {
        MainWindowComponentController.mainWindowStage = stage;
        stage.setTitle("Komunikator490");
        stage.setOnCloseRequest(closeWindowHandler);
    }

    public void initialize() {
        // only allow numeric values as port and IP numbers

        theirPort.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                theirPort.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        theirIp.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d*\\.)*")) {
                theirIp.setText(newValue.replaceAll("[^(\\d*.)]", ""));
            }
        });

        connectButton.setOnMouseClicked(mouseEvent -> {
            try { // TODO add conversation to list in Communicator
                openConversationWindow(new Conversation(InetAddress.getByName(theirIp.getText()), Integer.parseInt(theirPort.getText())));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }

    private EventHandler<WindowEvent> closeWindowHandler = windowEvent -> communicator.stop();


    public ConversationWindowController openConversationWindow(Conversation conversation) {
        ConversationWindowController conversationWindowController;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/communicator490/fxml/conversationWindow/conversationWindow.fxml"));
            Parent root = loader.load();
            conversationWindowController = loader.getController();
            Stage stage = new Stage();
            conversationWindowController.setStage(stage);
            conversationWindowController.setConversation(conversation);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            conversationWindowController = null; // TODO handle error
        }
        return conversationWindowController;
    }

}

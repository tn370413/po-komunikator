package com.communicator490.controllers.mainWindow;

import com.communicator490.Communicator;
import com.communicator490.Severity;
import com.communicator490.communication.Conversation;
import com.communicator490.controllers.Controller;
import com.communicator490.controllers.conversationWindow.ConversationWindowController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MainWindowController extends Controller {

    private Stage stage;

    @FXML
    private YourIdInfoController yourIdInfoControlController;

    @FXML
    private TextField theirIp;

    @FXML
    private TextField theirPort;

    @FXML
    private Button connectButton;

    @FXML
    private Label errorInfoLabel;

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setTitle("Komunikator490");
        stage.setOnCloseRequest(closeWindowHandler);

        stage.getScene().setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                connectButton.fire();
                keyEvent.consume();
            }
        }); // TODO set this on root element, not scene

        // prevent resizing of scene because of footer
        errorInfoLabel.setPrefWidth(stage.getWidth());
    }

    public void initialize() {
        // only allow numeric values as port and IP numbers (maybe let use computer names for IP?)

        theirPort.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                theirPort.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
//        theirIp.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("(\\d*\\.)*")) {
//                theirIp.setText(newValue.replaceAll("[^(\\d*.)]", ""));
//            }
//        });

        connectButton.setOnAction(actionEvent -> Communicator.getInstance().openConversation(theirIp.getText(), Integer.parseInt(theirPort.getText())));
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

            note("Opened conversation with " + conversation.getForeignAddress());
        } catch (IOException e) {
            handleWarning("Couldn't open conversation: " + e.getMessage());
            conversationWindowController = null;
        }
        return conversationWindowController;
    }

    private void note(String message) {
        errorInfoLabel.setText(message);
        errorInfoLabel.setId("EIL-note");
        stage.sizeToScene();
    }

    public void handleFatalError(String content) {
        errorInfoLabel.setText(content);
        errorInfoLabel.setId("EIL-error");

        theirIp.setVisible(false);
        theirPort.setVisible(false);
        connectButton.setText("EXIT"); // can be done prettier but good enough for this version
        connectButton.setOnAction(actionEvent -> Platform.exit());

        stage.sizeToScene();
        Communicator.getInstance().getLogger().log(stage.getTitle() + ": " + content, Severity.ERROR);
    }

    public void handleWarning(String content) {
        errorInfoLabel.setText(content);
        errorInfoLabel.setId("EIL-warning");
        stage.sizeToScene();

        Communicator.getInstance().getLogger().log(stage.getTitle() + ": " + content, Severity.WARNING);
    }
}

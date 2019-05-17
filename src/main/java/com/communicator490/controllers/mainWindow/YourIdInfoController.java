package com.communicator490.controllers.mainWindow;

import com.communicator490.controllers.Controller;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

// class to control the part of the main window that shows user's own IP and port

public class YourIdInfoController extends Controller {
    @FXML
    private Label yourIPText;

    @FXML
    private Label toggleNetworkToLocalLabel;

    @FXML
    private Label toggleNetworkToExternalLabel;

    @FXML
    private Label yourPortText;

    @FXML
    private VBox yourIdInfoVBox;

    public void initialize() {
        yourIdInfoVBox.getChildren().remove(toggleNetworkToExternalLabel);
        toggleToLocal();

        toggleNetworkToLocalLabel.setOnMouseClicked(toggleToLocalHandler);
//        toggleNetworkToExternalLabel.setOnMouseClicked(toggleToExternalHandler);

//        when communication through NAT is implemented it will be possible to switch between local and external IP
        yourIdInfoVBox.getChildren().remove(toggleNetworkToExternalLabel); // TODO external IP not implemented yet
    }

    private void toggleToLocal() {
        yourIPText.setText(communicator.getInternalIp());
        yourPortText.setText(String.format("%d", communicator.getInternalPort()));
        replace(toggleNetworkToLocalLabel, toggleNetworkToExternalLabel, yourIdInfoVBox.getChildren());
    }

//    private void toggleToExternal() {
//        yourIPText.setText(communicator.getIp());
//        yourPortText.setText(String.format("%d", communicator.getPort()));
//        replace(toggleNetworkToExternalLabel, toggleNetworkToLocalLabel, yourIdInfoVBox.getChildren());
//    }

    private EventHandler<MouseEvent> toggleToLocalHandler = mouseEvent -> toggleToLocal();
//    private EventHandler<MouseEvent> toggleToExternalHandler = mouseEvent -> toggleToExternal();
}

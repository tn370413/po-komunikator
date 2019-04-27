package com.communicator490.controllers.mainWindow;

import com.communicator490.controllers.guiController;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.Node;

public class mainWindowController extends guiController {

    public void initialize() {
        initializeChangePortBox();

        // TODO should set Your IP and Your port
    }


    /*
        Change port box
     */

    @FXML
    private Label changePortLabel;

    @FXML
    private Label changePortText;

    private TextField changePortTextField = new TextField();

    private Label saveNewPortLabel = new Label(" (Save)");

    private Label cancelNewPortLabel = new Label(" (Cancel)");

    @FXML
    private HBox changePortBox;

    private void initializeChangePortBox() {
        changePortTextField.setId("your-port-change-textfield");
        saveNewPortLabel.getStyleClass().add("your-port-change-label");
        cancelNewPortLabel.getStyleClass().add("your-port-change-label");

        changePortLabel.setOnMouseClicked(changePortHandler);
        saveNewPortLabel.setOnMouseClicked(savePortHandler);
        cancelNewPortLabel.setOnMouseClicked(cancelPortHandler);
    }

    private EventHandler<MouseEvent> changePortHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            ObservableList<Node> changePortBoxChildren = changePortBox.getChildren();
            replaceLabelTextField(changePortText, changePortTextField, changePortBoxChildren);
            int posChangePortLabel = changePortBoxChildren.indexOf(changePortLabel);
            changePortBoxChildren.remove(posChangePortLabel);
            changePortBoxChildren.add(posChangePortLabel, cancelNewPortLabel);
            changePortBoxChildren.add(posChangePortLabel, saveNewPortLabel);
            stage.sizeToScene();
        }
    };

    private void saveCancelPortAux(ObservableList<Node> changePortBoxChildren) {
        int posChangePortLabel = changePortBoxChildren.indexOf(cancelNewPortLabel);
        changePortBoxChildren.add(posChangePortLabel, changePortLabel);
        changePortBoxChildren.remove(cancelNewPortLabel);
        changePortBoxChildren.remove(saveNewPortLabel);
        stage.sizeToScene();
    }

    private EventHandler<MouseEvent> savePortHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            communicator.setPort(Integer.parseInt(changePortTextField.getText()));

            ObservableList<Node> changePortBoxChildren = changePortBox.getChildren();
            replaceTextFieldLabel(changePortTextField, changePortText, changePortBoxChildren);
            saveCancelPortAux(changePortBoxChildren);
        }
    };

    private EventHandler<MouseEvent> cancelPortHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            ObservableList<Node> changePortBoxChildren = changePortBox.getChildren();
            replaceTextFieldLabel(changePortTextField, changePortText, changePortBoxChildren);
            changePortText.setText(String.format("%d", communicator.getPort()));
            saveCancelPortAux(changePortBoxChildren);
        }
    };

    /*

     */
}

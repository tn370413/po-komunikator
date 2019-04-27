package com.communicator490.controllers.mainWindow;

import com.communicator490.controllers.GuiController;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainWindowComponentController extends GuiController {
    protected void replaceLabelTextField(Label label, TextField textField, ObservableList<Node> siblings) {
        int pos = siblings.indexOf(label);
        siblings.remove(label);
        textField.setText(label.getText());
        siblings.add(pos, textField);
    }

    protected void replaceTextFieldLabel(TextField textField, Label label, ObservableList<Node> siblings) {
        int pos = siblings.indexOf(textField);
        siblings.remove(textField);
        label.setText(textField.getText());
        siblings.add(pos, label);
    }

    protected static Stage mainWindowStage;

    public static void setStage(Stage stage) {
        MainWindowComponentController.mainWindowStage = stage;
        stage.setTitle("Komunikator490");
    }
}

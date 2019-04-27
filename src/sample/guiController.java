package sample;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class guiController {
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

    protected Stage stage;

    protected Communicator communicator;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }
}

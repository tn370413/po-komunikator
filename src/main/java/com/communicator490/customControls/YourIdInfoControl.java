package com.communicator490.customControls;

import java.io.IOException;

import com.communicator490.controllers.mainWindow.YourIdInfoController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class YourIdInfoControl extends VBox {
    public YourIdInfoControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/com/communicator490/fxml/mainWindow/yourIdInfo.fxml"));
        fxmlLoader.setRoot(this);
        YourIdInfoController controller = new YourIdInfoController();
        fxmlLoader.setController(controller);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}

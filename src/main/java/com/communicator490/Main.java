package com.communicator490;

import com.communicator490.controllers.mainWindow.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// this class is only to start the application, main controller is in Communicator class

// throughout the project we use public modifier 'public' also in places where given method could be package-private
// (default visibility), to comply with the advice from the PO lecture that default visibility should not be used

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        loads main application window

//        note! the order of these calls IS important
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/communicator490/fxml/mainWindow/mainWindow.fxml"));
        Parent root = loader.load();
        MainWindowController mainWindowController = loader.getController();
        Communicator.getInstance().setMainWindowController(mainWindowController);
        Scene scene = new Scene(root);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        mainWindowController.setStage(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

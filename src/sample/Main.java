package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Communicator communicator;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Parent root = loader.load();
        mainWindowController controller = loader.getController();
        controller.setStage(primaryStage);
        controller.setCommunicator(communicator);
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root);//), 300, 275);
        scene.getStylesheets().add(getClass().getResource("mainWindow.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        communicator = new Communicator();
        launch(args);
    }
}

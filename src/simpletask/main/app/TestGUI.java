package simpletask.main.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Temp class to test GUI.
 *
 * @author Matthew Taggart
 */
public class TestGUI extends Application {
    @Override
    public void start(final Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../gui/resources/Workspace.fxml"));
        primaryStage.setTitle("Simple Task");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
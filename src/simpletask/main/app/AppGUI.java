package simpletask.main.app;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import simpletask.main.entities.WorkspaceManager;

/**
 * Temp class to test GUI.
 *
 * @author Matthew Taggart
 */
public class TestGUI extends Application {
    private static Stage primaryStage;

    private static Config config;

    public static Stage getStage() {
        return primaryStage;
    }

    private void setStage(final Stage primaryStage) {
        TestGUI.primaryStage = primaryStage;
    }
    @Override
    public void start(final Stage primaryStage) throws Exception {
        if (null == config.getConfig(ConfigKeys.DIR)) {
            // Show file explorer and configure .config
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            fileChooser.setTitle("Select File To Save your Workspace");
            File workspace = fileChooser.showSaveDialog(primaryStage);
            if (null == workspace) {
                System.exit(0);
            }
            WorkspaceManager.initialise(workspace.getName());
            WorkspaceManager.getInstance().save(workspace.getAbsolutePath());
            config.setValue(ConfigKeys.DIR, workspace.getAbsolutePath());
            config.saveCurrentSettings();
        }
        Parent root = FXMLLoader.load(getClass().getResource("../gui/resources/Workspace.fxml"));
        setStage(primaryStage);
        primaryStage.setTitle("Simple Task");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }
    /**
     * On initialisation, the app needs to load in the WorkspaceManager from the saved location.
     * This sets up the singleton instance.
     */
    @Override
    public void init() throws Exception {
        config = new Config(".config");
        // Load the saved data into the WorkspaceManager
        if (null != config.getConfig(ConfigKeys.DIR)) {
            WorkspaceManager.loadWorkspace(config.getConfig(ConfigKeys.DIR));
        }
    }
    /**
     * The WorkdspaceManager should be saved on exit.
     */
    @Override
    public void stop() throws Exception {
        WorkspaceManager.getInstance().save(config.getConfig(ConfigKeys.DIR));
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
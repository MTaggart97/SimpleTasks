package simpletask.main.gui.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import simpletask.main.entities.NodeData;
import simpletask.main.entities.NodeKeys;
import simpletask.main.entities.WorkspaceManager;
import simpletask.main.gui.Manager;

/**
 * Controller for main window.
 *
 * @author Matthew Taggart
 */
public class MainController {
    /**
     * This is the BorderPane of the main window.
     */
    @FXML
    private BorderPane mainBorderPane;
    /**
     * Reference to the HBox in the main window.
     */
    @FXML
    private HBox mainHBox;
    /**
     * Reference to SplitPane in main window.
     */
    @FXML
    private SplitPane splitPane;
    /**
     * Reference to WorkspaceName in main window.
     */
    @FXML
    private Text workspaceName;
    // TODO: Add the rest of the fields

    /**
     * Actions to take on initialisation. Loads the workspace into the gui.
     */
    // @SuppressWarnings({"unchecked"})
    @FXML
    public void initialize() {
        clearWorkspace();
        ArrayList<VBox> vboxs = Manager.getInstance().loadWorkspace();
        workspaceName.setText(WorkspaceManager.getInstance().getCurrentWorkspaceDetails().getAttr(NodeKeys.NAME));
        for (VBox b : vboxs) {
            mainHBox.getChildren().add(b);
            WorkspaceManager.getInstance().stepIntoWorkspace(mainHBox.getChildren().indexOf(b));
            for (NodeData item : WorkspaceManager.getInstance().getTasks()) {
                ((ListView<NodeData>) b.getChildren().get(1)).getItems().add(item);
            }
            WorkspaceManager.getInstance().stepUp();
        }
    }

    // TODO: Create a method to populate the bottom UI based on the data in Manager.

    /**
     * Used to show the New Card Dialog. Uses the NewCardDialog.fxml file, sets it's
     * parent to be the mainBorderPane.
     */
    @FXML
    public void showNewCardDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Create New Card");
        // dialog.setHeaderText("Use this dialog to create a new todo item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../resources/NewCardDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Unable to load dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewCardDialogController controller = fxmlLoader.getController();
            VBox card = controller.processInput();
            mainHBox.getChildren().add(card);
        }
    }

    //TODO: Add logic to populate the bottom of UI based of NodeData instance

    /**
     * Used to save workspace.
     */
    @FXML
    public void saveWorkspace() {
        System.out.println("Saving Workspace...");
        WorkspaceManager.getInstance().save("SavedWorkspace/workspace.ser");
        System.out.println("Saved");
    }

    @FXML
    public void moveUp() {
        WorkspaceManager.getInstance().stepUp();
        initialize();
    }

    @FXML
    public void moveHome() {
        WorkspaceManager.getInstance().home();
        initialize();
    }

    /**
     * Clears current workspace.
     */
    public void clearWorkspace() {
        mainHBox.getChildren().clear();
    }
}
package simpletask.main.gui.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import simpletask.main.app.Config;
import simpletask.main.app.ConfigKeys;
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
    private Config config;
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
    /**
     * 
     */
    @FXML
    private ComboBox<String> subNodeType;

    @FXML
    private Label subNodeTasks;

    @FXML
    private Button subNodeSave;

    @FXML
    private TextField subNodePriority;

    @FXML
    private TextField subNodeName;

    @FXML
    private DatePicker subNodeDueDate;

    @FXML
    private TextArea subNodeDesc;

    @FXML
    private RadioButton subNodeComplete;

    @FXML
    private ComboBox<String> mainNodeType;

    @FXML
    private Button mainNodeSave;

    @FXML
    private TextField mainNodePriority;

    @FXML
    private TextField mainNodeName;

    @FXML
    private DatePicker mainNodeDueDate;

    @FXML
    private TextArea mainNodeDesc;

    @FXML
    private RadioButton mainNodeComplete;

    public MainController() {
        config = new Config(".config");
    }
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
            ((ListView<NodeData>) b.getChildren().get(1)).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                        if(mouseEvent.getClickCount() == 1) {
                            populateSummaryUI();
                        }
                    }
                }
            });
        }
        mainNodeType.getItems().addAll("Task","Action");
        subNodeType.getItems().addAll("Task","Action");
        populateSummaryUI();
    }

    /**
     * 
     */
    private void populateSummaryUI() {
        populateMainTask(Manager.getInstance().mainTaskData);
        populateSubTask(Manager.getInstance().subTaskData);
    }

    private void populateMainTask(final NodeData mainTask) {
        if (null != mainTask) {
            mainNodeName.setText(mainTask.getAttr(NodeKeys.NAME));
            mainNodeDesc.setText(mainTask.getAttr(NodeKeys.DESCRIPTION));
            mainNodePriority.setText(mainTask.getAttr(NodeKeys.PRIORITY));
            mainNodeComplete.setSelected(Boolean.valueOf(mainTask.getAttr(NodeKeys.COMPLETE)));
            mainNodeType.getSelectionModel().select(mainTask.getAttr(NodeKeys.TYPE));
            mainNodeDueDate.setValue(LocalDate.parse(mainTask.getAttr(NodeKeys.DUEDATE).substring(0, 10)));
        }
    }

    private void populateSubTask(final NodeData subTask) {
        if (null != subTask) {
            subNodeName.setText(subTask.getAttr(NodeKeys.NAME));
            subNodeDesc.setText(subTask.getAttr(NodeKeys.DESCRIPTION));
            subNodePriority.setText(subTask.getAttr(NodeKeys.PRIORITY));
            subNodeComplete.setSelected(Boolean.valueOf(subTask.getAttr(NodeKeys.COMPLETE)));
            subNodeType.getSelectionModel().select(subTask.getAttr(NodeKeys.TYPE));
            subNodeDueDate.setValue(LocalDate.parse(subTask.getAttr(NodeKeys.DUEDATE).substring(0, 10)));
            subNodeTasks.setText(subTask.getAttr(NodeKeys.TASKS));
        }
    }

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

    /**
     * Used to save workspace.
     */
    @FXML
    public void saveWorkspace() {
        System.out.println("Saving Workspace...");
        WorkspaceManager.getInstance().save(config.getConfig(ConfigKeys.DIR));
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

    @FXML
    public void saveMainNode() {
        int editIndex = WorkspaceManager.getInstance().getTasks().indexOf(Manager.getInstance().mainTaskData);
        WorkspaceManager.getInstance().stepIntoWorkspace(editIndex);
        // Save Workspace
        WorkspaceManager.getInstance().setName(mainNodeName.getText().trim());
        WorkspaceManager.getInstance().setDescription(mainNodeDesc.getText().trim());
        WorkspaceManager.getInstance().setPriority(mainNodePriority.getText());
        WorkspaceManager.getInstance().setComplete(String.valueOf(mainNodeComplete.isSelected()));
        WorkspaceManager.getInstance().setDueDate(mainNodeDueDate.getValue().toString());
        WorkspaceManager.getInstance().setType(mainNodeType.getValue());
        
        WorkspaceManager.getInstance().stepUp();

        // Save GUI version
        ((Text) ((VBox) mainHBox.getChildren().get(editIndex)).getChildren().get(0)).setText(mainNodeName.getText().trim());

        // NodeData editData = new NodeData();
        // editData.setAttr(NodeKeys.NAME, mainNodeName.getText().trim());
        // editData.setAttr(NodeKeys.DESCRIPTION, mainNodeDesc.getText().trim());
        // editData.setAttr(NodeKeys.PRIORITY, mainNodePriority.getText());
        // editData.setAttr(NodeKeys.COMPLETE, String.valueOf(mainNodeComplete.isSelected()));
        // editData.setAttr(NodeKeys.DUEDATE, mainNodeDueDate.getValue().toString());
        // editData.setAttr(NodeKeys.TYPE, mainNodeType.getValue());
        // editData.setAttr(NodeKeys.TASKS, Manager.getInstance().mainTaskData.getAttr(NodeKeys.TASKS));
        // Manager.getInstance().editAtIndex(new int[] {editIndex}, editData);
    }

    public void saveSubNode() {
        int mainEditIndex = WorkspaceManager.getInstance().getTasks().indexOf(Manager.getInstance().mainTaskData);
        WorkspaceManager.getInstance().stepIntoWorkspace(mainEditIndex);
        int subEditIndex = WorkspaceManager.getInstance().getTasks().indexOf(Manager.getInstance().subTaskData);
        WorkspaceManager.getInstance().stepIntoWorkspace(subEditIndex);
        // Save Workspace
        WorkspaceManager.getInstance().setName(subNodeName.getText().trim());
        WorkspaceManager.getInstance().setDescription(subNodeDesc.getText().trim());
        WorkspaceManager.getInstance().setPriority(subNodePriority.getText());
        WorkspaceManager.getInstance().setComplete(String.valueOf(subNodeComplete.isSelected()));
        WorkspaceManager.getInstance().setDueDate(subNodeDueDate.getValue().toString());
        WorkspaceManager.getInstance().setType(subNodeType.getValue());

        WorkspaceManager.getInstance().stepUp();
        WorkspaceManager.getInstance().stepUp();

        // Save GUI version
        NodeData editData = new NodeData();
        editData.setAttr(NodeKeys.NAME, subNodeName.getText().trim());
        editData.setAttr(NodeKeys.DESCRIPTION, subNodeDesc.getText().trim());
        editData.setAttr(NodeKeys.PRIORITY, subNodePriority.getText());
        editData.setAttr(NodeKeys.COMPLETE, String.valueOf(subNodeComplete.isSelected()));
        editData.setAttr(NodeKeys.DUEDATE, subNodeDueDate.getValue().toString());
        editData.setAttr(NodeKeys.TYPE, subNodeType.getValue());
        editData.setAttr(NodeKeys.TASKS, Manager.getInstance().subTaskData.getAttr(NodeKeys.TASKS));
        Manager.getInstance().editAtIndex(new int[] {mainEditIndex, subEditIndex}, editData);
    }
}
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
import simpletask.main.gui.VBoxWrapper;

/**
 * Controller for main window.
 *
 * @author Matthew Taggart
 */
public class MainController {
    /**
     * The config that the application is using.
     */
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
     * The combo box in the sub node summary pane.
     */
    @FXML
    private ComboBox<String> subNodeType;
    /**
     * The task in the sub node summary pane.
     */
    @FXML
    private Label subNodeTasks;
    /**
     * The save button in the sub node summary pane.
     */
    @FXML
    private Button subNodeSave;
    /**
     * The priority in the sub node summary pane.
     */
    @FXML
    private TextField subNodePriority;
    /**
     * The name in the sub node summary pane.
     */
    @FXML
    private TextField subNodeName;
    /**
     * The due date in the sub node summary pane.
     */
    @FXML
    private DatePicker subNodeDueDate;
    /**
     * The description in the sub node summary pane.
     */
    @FXML
    private TextArea subNodeDesc;
    /**
     * The completion in the sub node summary pane.
     */
    @FXML
    private RadioButton subNodeComplete;
    /**
     * The combo box in the main node summary pane.
     */
    @FXML
    private ComboBox<String> mainNodeType;
    /**
     * The save button in the main node summary pane.
     */
    @FXML
    private Button mainNodeSave;
    /**
     * The priority in the main node summary pane.
     */
    @FXML
    private TextField mainNodePriority;
    /**
     * The name in the main node summary pane.
     */
    @FXML
    private TextField mainNodeName;
    /**
     * The due date in the main node summary pane.
     */
    @FXML
    private DatePicker mainNodeDueDate;
    /**
     * The description in the main node summary pane.
     */
    @FXML
    private TextArea mainNodeDesc;
    /**
     * The completion status in the main node summary pane.
     */
    @FXML
    private RadioButton mainNodeComplete;
    /**
     * The lenght of the date part of a DateTime when represented as a string.
     */
    private final int dateLenth = 10;
    /**
     * No args constructor that will set the config to be based of the .config file. If
     * it does not exist, one will be created.
     */
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
        ArrayList<VBoxWrapper> vboxs = Manager.getInstance().loadWorkspace();
        workspaceName.setText(WorkspaceManager.getInstance().getCurrentWorkspaceDetails().getAttr(NodeKeys.NAME));
        for (VBoxWrapper b : vboxs) {
            mainHBox.getChildren().add(b.getVBox());
            WorkspaceManager.getInstance().stepIntoWorkspace(mainHBox.getChildren().indexOf(b.getVBox()));
            for (NodeData item : WorkspaceManager.getInstance().getTasks()) {
                b.getListView().getItems().add(item);
            }
            WorkspaceManager.getInstance().stepUp();
            b.getListView().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(final MouseEvent mouseEvent) {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        if (mouseEvent.getClickCount() == 1) {
                            populateSummaryUI();
                        }
                    }
                }
            });
        }
        mainNodeType.getItems().addAll("Task", "Action");
        subNodeType.getItems().addAll("Task", "Action");
        populateSummaryUI();
    }

    /**
     * Populates the main and sub summary fields with what is currently in the Manager's
     * main/subTaskData fields.
     */
    private void populateSummaryUI() {
        populateMainTask(Manager.getInstance().getMainTaskData());
        populateSubTask(Manager.getInstance().getSubTaskData());
    }
    /**
     * Populates the main task summary field. If it is null, nothing happens.
     *
     * @param mainTask  The data to populate it with
     */
    private void populateMainTask(final NodeData mainTask) {
        if (null != mainTask) {
            // Reset combo list
            mainNodeType.setDisable(false);

            mainNodeName.setText(mainTask.getAttr(NodeKeys.NAME));
            mainNodeDesc.setText(mainTask.getAttr(NodeKeys.DESCRIPTION));
            mainNodePriority.setText(mainTask.getAttr(NodeKeys.PRIORITY));
            mainNodeComplete.setSelected(Boolean.valueOf(mainTask.getAttr(NodeKeys.COMPLETE)));
            mainNodeType.getSelectionModel().select(mainTask.getAttr(NodeKeys.TYPE));
            mainNodeDueDate.setValue(LocalDate.parse(mainTask.getAttr(NodeKeys.DUEDATE).substring(0, dateLenth)));
            // If this task has sub tasks, then you cannot change the type
            if (!mainTask.getAttr(NodeKeys.TASKS).equals("0")) {
                mainNodeType.setDisable(true);
            }
        }
    }
    /**
     * Populates the sub task summary fied. If it is null, nothing happens.
     *
     * @param subTask   The data to populate it with
     */
    private void populateSubTask(final NodeData subTask) {
        if (null != subTask) {
            // Reset combo list
            subNodeType.setDisable(false);

            subNodeName.setText(subTask.getAttr(NodeKeys.NAME));
            subNodeDesc.setText(subTask.getAttr(NodeKeys.DESCRIPTION));
            subNodePriority.setText(subTask.getAttr(NodeKeys.PRIORITY));
            subNodeComplete.setSelected(Boolean.valueOf(subTask.getAttr(NodeKeys.COMPLETE)));
            subNodeType.getSelectionModel().select(subTask.getAttr(NodeKeys.TYPE));
            subNodeDueDate.setValue(LocalDate.parse(subTask.getAttr(NodeKeys.DUEDATE).substring(0, dateLenth)));
            subNodeTasks.setText(subTask.getAttr(NodeKeys.TASKS));
            // If this task has sub tasks, then you cannot change the type
            if (!subTask.getAttr(NodeKeys.TASKS).equals("0")) {
                subNodeType.setDisable(true);
            }
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
    /**
     * Moves the current workspace up a level and redraws the scene.
     */
    @FXML
    public void moveUp() {
        WorkspaceManager.getInstance().stepUp();
        initialize();
    }
    /**
     * Moves the current workspace to the root level and redraws the scene.
     */
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
    /**
     * Saves the main task from the summary pane. The WorkspaceManager will save the actual workspace. Note that when
     * saving the GUI version, only the name needs to be updated. This is because it is the only attribute to be shown.
     */
    @FXML
    public void saveMainNode() {
        int editIndex = WorkspaceManager.getInstance().getTasks().indexOf(Manager.getInstance().getMainTaskData());
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
    }
    /**
     * Saves the sub task from the summary pane. The WorkspaceManager will update the underlying workspace and then the
     * Manager will update the GUI version.
     */
    public void saveSubNode() {
        int mainEditIndex = WorkspaceManager.getInstance().getTasks().indexOf(Manager.getInstance().getMainTaskData());
        WorkspaceManager.getInstance().stepIntoWorkspace(mainEditIndex);
        int subEditIndex = WorkspaceManager.getInstance().getTasks().indexOf(Manager.getInstance().getSubTaskData());
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
        editData.setAttr(NodeKeys.TASKS, Manager.getInstance().getSubTaskData().getAttr(NodeKeys.TASKS));
        Manager.getInstance().editAtIndex(new int[] {mainEditIndex, subEditIndex}, editData);
    }
}

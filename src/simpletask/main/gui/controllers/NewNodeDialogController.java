package simpletask.main.gui.controllers;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import simpletask.main.entities.NodeData;
import simpletask.main.entities.NodeKeys;

public class NewNodeDialogController {
    @FXML
    private TextField newNodeName;
    @FXML
    private TextArea newNodeDesc;
    @FXML
    private DatePicker newNodeDueDate;
    @FXML
    private TextField newNodePriority;
    @FXML
    private ComboBox<String> newNodeComboBox;
    @FXML
    private RadioButton isComplete;
    @FXML
    private Label numOfSubTasks;

    /**
     * Actions to take on initialisation. Sets up ComboBox contents.
     */
    @FXML
    public void initialize() {
        newNodeComboBox.getItems().addAll("Task","Action");
        newNodeComboBox.getSelectionModel().select("Task");
    }
    
    /**
     * Takes the user inputs and creates a dictionary that can be used to
     * add to the list of tasks. Validation checking should be done here.
     *
     * @return  A dictionary representing the new item
     */
    public NodeData processInputs() {
        final NodeData input = new NodeData();

        input.setAttr(NodeKeys.NAME, null == newNodeName ? null : newNodeName.getText().trim());
        input.setAttr(NodeKeys.DESCRIPTION, null == newNodeDesc ? null : newNodeDesc.getText().trim());
        input.setAttr(NodeKeys.PRIORITY, null == newNodePriority ? null : newNodePriority.getText().trim());
        input.setAttr(NodeKeys.DUEDATE, null == newNodeDueDate.getValue() ? LocalDate.now().toString() : newNodeDueDate.getValue().toString());
        input.setAttr(NodeKeys.TYPE, null == newNodeComboBox.getValue() ? null : newNodeComboBox.getValue().trim());
        input.setAttr(NodeKeys.TASKS, null == numOfSubTasks ? null : numOfSubTasks.getText().trim());
        input.setAttr(NodeKeys.COMPLETE, null == isComplete ? null : String.valueOf(isComplete.isSelected()));

        return input;
    }

    public NewNodeDialogController setNewNodeName(final String newName) {
        newNodeName.setText(newName);
        return this;
    }

    public NewNodeDialogController setNewNodeDesc(final String newDesc) {
        newNodeDesc.setText(newDesc);
        return this;
    }

    public NewNodeDialogController setNewNodeProirity(final String newPriority) {
        newNodePriority.setText(newPriority);
        return this;
    }

    public NewNodeDialogController setNewNodeDueDate(final String newDate) {
        // TODO: Need to create a DateTime Picker in JavaFX so that substring is not needed here
        newNodeDueDate.setValue(LocalDate.parse(newDate.substring(0, 10)));
        return this;
    }

    public NewNodeDialogController setNewNodeType(final String newType) {
        newNodeComboBox.getSelectionModel().select(newType);
        return this;
    }

    public NewNodeDialogController setNewNodeComplete(final String complete) {
        isComplete.setSelected(Boolean.valueOf(complete));
        return this;
    }

    public NewNodeDialogController setNewNodeTasks(final String numOfTasks) {
        numOfSubTasks.setText(numOfTasks);
        return this;
    }
    
}
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

/**
 * Controller for the new node dialog scene. This is used to create a new task, giving the
 * user the option to fill in all fields (not just the name like the NewCard dialog).
 * <p>
 * Note that the "setters" only set the values of the NewNodeDialogController object. They
 * do not set what is actually under the hood. These setters are called by the Manager to
 * initially populate the GUI, which the user then edits. processInputs() is then called to
 * to get an object which can be used to edit what is under the hood.
 */
public class NewNodeDialogController {
    /**
     * Field to input name.
     */
    @FXML
    private TextField newNodeName;
    /**
     * Field to input description.
     */
    @FXML
    private TextArea newNodeDesc;
    /**
     * Field to input due date.
     */
    @FXML
    private DatePicker newNodeDueDate;
    /**
     * Field to input priority.
     */
    @FXML
    private TextField newNodePriority;
    /**
     * Field to input node type.
     */
    @FXML
    private ComboBox<String> newNodeComboBox;
    /**
     * Field to input completion status.
     */
    @FXML
    private RadioButton isComplete;
    /**
     * Label to show number of tasks.
     */
    @FXML
    private Label numOfSubTasks;
    /**
     * Lenght of the date part of a DateTime when represented as a string.
     */
    private final int dateLength = 10;

    /**
     * Actions to take on initialisation. Sets up ComboBox contents.
     */
    @FXML
    public void initialize() {
        newNodeComboBox.getItems().addAll("Task", "Action");
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
    /**
     * Sets the nodes name.
     *
     * @param newName   Name to rename to
     * @return          This instance
     */
    public NewNodeDialogController setNewNodeName(final String newName) {
        newNodeName.setText(newName);
        return this;
    }
    /**
     * Sets the description.
     *
     * @param newDesc   Description to set
     * @return          This instance
     */
    public NewNodeDialogController setNewNodeDesc(final String newDesc) {
        newNodeDesc.setText(newDesc);
        return this;
    }
    /**
     * Sets the priority of the dialog.
     *
     * @param newPriority   Priority to set to
     * @return              This instance
     */
    public NewNodeDialogController setNewNodeProirity(final String newPriority) {
        newNodePriority.setText(newPriority);
        return this;
    }
    /**
     * Sets the due date of the dialog.
     *
     * @param newDate   Date to set to
     * @return          This instance
     */
    public NewNodeDialogController setNewNodeDueDate(final String newDate) {
        // TODO: Need to create a DateTime Picker in JavaFX so that substring is not needed here
        newNodeDueDate.setValue(LocalDate.parse(newDate.substring(0, dateLength)));
        return this;
    }
    /**
     * Sets the type of the dialog.
     *
     * @param newType   The new type to set
     * @return          This instance
     */
    public NewNodeDialogController setNewNodeType(final String newType) {
        newNodeComboBox.getSelectionModel().select(newType);
        return this;
    }
    /**
     * Sets the completion status of the dialog.
     *
     * @param complete  New completion status
     * @return          This instance
     */
    public NewNodeDialogController setNewNodeComplete(final String complete) {
        isComplete.setSelected(Boolean.valueOf(complete));
        return this;
    }
    /**
     * Sets the new number of sub tasks of the dialog.
     *
     * @param numOfTasks    The new number of tasks
     * @return              This instance
     */
    public NewNodeDialogController setNewNodeTasks(final String numOfTasks) {
        numOfSubTasks.setText(numOfTasks);
        return this;
    }
}

package simpletask.main.gui.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
    public Map<String, String> processInputs() {
        final Map<String, String> input = new HashMap<>();

        input.put("Name", newNodeName.getText().trim());
        input.put("Description", newNodeDesc.getText().trim());
        input.put("Priority", newNodePriority.getText().trim());
        input.put("DueDate", newNodeDueDate.getValue().toString());
        input.put("Type", newNodeComboBox.getValue().trim());

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
    
}
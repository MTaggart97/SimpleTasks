package simpletask.main.gui.controllers;

import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NewNodeDialogController {
    @FXML
    private TextField newNodeName;
    @FXML
    private TextArea newNodeDesc;
    // @FXML
    // private DatePicker newNodeDate;
    @FXML
    private TextField newNodePriority;

    /**
     * Takes the user inputs and creates a dictionary that can be used to
     * add to the list of tasks. Validation checking should be done here.
     *
     * @return  A dictionary representing the new item
     */
    public Map<String, String> processInputs() {
        Map<String, String> input = new HashMap<>();

        input.put("Name", newNodeName.getText().trim());
        input.put("Description", newNodeDesc.getText().trim());
        input.put("Priority", newNodePriority.getText().trim());
        // input.put("DueDate", newNodeDate.getValue().toString());

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
    
}
package simpletask.main.gui.controllers;

import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
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

    public Map<String, String> processInputs() {
        Map<String, String> input = new HashMap<>();

        input.put("Name", newNodeName.getText().trim());
        input.put("Description", newNodeDesc.getText().trim());
        input.put("Priority", newNodePriority.getText().trim());
        // input.put("DueDate", newNodeDate.getValue().toString());

        return input;
    }
    
}
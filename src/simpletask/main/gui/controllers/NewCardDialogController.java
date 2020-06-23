package simpletask.main.gui.controllers;

import java.io.IOException;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import simpletask.main.gui.Manager;

public class NewCardDialogController {
    /**
     * Name that user enters.
     */
    @FXML
    private TextField NewCardName;
    /**
     * Used to create a task card for the current workspace. Using the Card.fxml file,
     * it will create a VBox with the title inputted by the user and an empty ListView.
     *
     * @return  The card that will be added to workspace
     */
    public VBox processInput() {
        String name = NewCardName.getText().trim();
        // WorkspaceManager.getInstance().addWorkspace(name, "Task");
        ListView<Map<String,String>> newList = Manager.getInstance().addToWorkspace(name);
        VBox card;
        try {
            card = FXMLLoader.load(getClass().getResource("../resources/Card.fxml"));
        } catch (IOException e) {
            System.err.println("Unable to load FXML");
            e.printStackTrace();
            return null;
        }
        ((Text) card.getChildren().get(0)).setText(name);
        card.getChildren().remove(1);
        card.getChildren().add(newList);
        
        return card;
    }
}
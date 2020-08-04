package simpletask.main.gui.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import simpletask.main.entities.NodeData;
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
        ListView<NodeData> newList = Manager.getInstance().addToWorkspace(name);
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
        //TODO: Try to get list view to grow
        VBox.setVgrow(newList, Priority.ALWAYS);
        newList.setMaxSize(200, Double.MAX_VALUE);
        newList.setMinSize(200, 300);
        card.getChildren().add(newList);
        
        return card;
    }
}
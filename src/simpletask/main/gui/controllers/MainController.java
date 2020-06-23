package simpletask.main.gui.controllers;

import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;

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
     * Used to show the New Card Dialog. Uses the NewCardDialog.fxml file, sets it's parent
     * to be the mainBorderPane.
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
            // DialogController controller = fxmlLoader.getController();
            // ToDoItem newItem = controller.processResults();
            System.out.println("OK pressed");
        } else {
            System.out.println("Cancel pressed");
        }
    }
}
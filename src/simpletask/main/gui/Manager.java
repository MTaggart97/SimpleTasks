package simpletask.main.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;
import simpletask.main.entities.WorkspaceManager;
import simpletask.main.gui.controllers.NewNodeDialogController;

/**
 * Singleton class that talks to the WorkspaceManager. It will hold the
 * underlying data for JavaFX (i.e. all the ObservalbeLists) which it will get
 * from the WorkspaceManager. It will hold a JavaFX version of what the
 * WorkspaceManager has. Handles any actions/context menus corresponding to these
 * lists.
 *
 * @author Matthew Taggart
 */
public class Manager {
    @FXML
    private List<ListView<Map<String, String>>> workspace;

    private static final Manager manager = new Manager();

    private Manager() {
        workspace = new ArrayList<>();
    };
    /**
     * Gets the instance of this singleton.
     *
     * @return  The instance of Manager
     */
    public static Manager getInstance() {
        return manager;
    }
    /**
     * Adds a new ObservableList to the workspace.
     *
     * @param   obsList List to add
     */
    public void addList(final ListView<Map<String,String>> obsList) {
        workspace.add(obsList);
    }

    /**
     * Adds the element to the ObservableList in the workspace.
     *
     * @param element   Element to add to the ObservableList
     * @param obsList   The list to add to
     */
    public void addToList(final ListView<Map<String,String>> obsList, final Map<String, String> element) {
        workspace.get(workspace.indexOf(obsList)).getItems().add(element);
    }

    public ListView<Map<String,String>> addToWorkspace(final String name) {
        WorkspaceManager.getInstance().addWorkspace(name, "Task");
        ListView<Map<String,String>> obsList = new ListView<>();

        // TODO: Needs to be handled better. Ideally set in one place with options blurred out if they are invalid
        addContextMenu(obsList);
        addCellFactory(obsList);

        workspace.add(obsList);
        return obsList;
    }

    private void addContextMenu(final ListView<Map<String,String>> obsList) {
        ContextMenu listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Delete");
            }
        });
        MenuItem editMenuItem = new MenuItem("Edit");
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Edit");
            }
        });
        MenuItem addMenuItem = new MenuItem("Add");
        addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Add");
                addItemToList(obsList, event);
            }
        });
        listContextMenu.getItems().addAll(addMenuItem, deleteMenuItem, editMenuItem);
        obsList.setContextMenu(listContextMenu);
    }

    private void addCellFactory(final ListView<Map<String,String>> obsList) {
        obsList.setCellFactory(new Callback<ListView<Map<String,String>>,ListCell<Map<String,String>>>(){
            @Override
            public ListCell<Map<String,String>> call(ListView<Map<String,String>> param) {
                ListCell<Map<String,String>> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Map<String,String> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.get("Name"));
                        }
                    }

                    @Override
                    public void startEdit() {
                        setText(getListView().getSelectionModel().getSelectedItem().get("Name"));
                    }
                };

                ContextMenu listContextMenu = new ContextMenu();
                MenuItem deleteMenuItem = new MenuItem("Delete");
                deleteMenuItem.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Delete Item");
                    }
                });
                MenuItem editMenuItem = new MenuItem("Edit");
                editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Edit Item");
                    }
                });
                MenuItem addMenuItem = new MenuItem("Add");
                addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Add");
                        addItemToList(obsList, event);
                    }
                });
                listContextMenu.getItems().addAll(addMenuItem, deleteMenuItem, editMenuItem);

                cell.emptyProperty().addListener(
                    (obs, wasEmpty, isNowEmpty) -> {
                        cell.setContextMenu(listContextMenu);
                    }
                );
                
                return cell;
            }
        });
    }

    private void deleteItem() {

    }

    private void editItem() {

    }
    /**
     * Executed when the user clicks "Add" through the Context menu. It will add a new
     * item to the corresponding list. As well notifying the WorkspaceManager of the newly
     * added item.
     *
     * @param obsList
     * @param event
     */
    private void addItemToList(final ListView<Map<String,String>> obsList, final ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit ToDo Item");
        dialog.setHeaderText("Use this dialog to edit todo item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("resources/NewNodeDialog.fxml"));
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
            NewNodeDialogController controller = fxmlLoader.getController();
            Map<String, String> newItem = controller.processInputs();
            WorkspaceManager.getInstance().stepIntoWorkspace(workspace.indexOf(obsList));
            WorkspaceManager.getInstance().addWorkspace(newItem);
            WorkspaceManager.getInstance().stepUp();
            addToList(workspace.get(workspace.indexOf(obsList)), newItem);
        } else {
            System.out.println("Cancel pressed");
        }
    }
}
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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

    public ArrayList<VBox> loadWorkspace() {
        ArrayList<VBox> vBoxs = new ArrayList<>();
        for (Map<String, String> w: WorkspaceManager.getInstance().getTasks()) {
            ListView<Map<String,String>> newList = Manager.getInstance().addNewList();
            VBox card;
            try {
                card = FXMLLoader.load(getClass().getResource("resources/Card.fxml"));
            } catch (IOException e) {
                System.err.println("Unable to load FXML");
                e.printStackTrace();
                return null;
            }
            ((Text) card.getChildren().get(0)).setText(w.get("Name"));
            card.getChildren().remove(1);
            //TODO: Try to get list view to grow
            VBox.setVgrow(newList, Priority.ALWAYS);
            newList.setMaxSize(200, Double.MAX_VALUE);
            newList.setMinSize(200, 300);
            card.getChildren().add(newList);
            // addList(newList);
            // mainHBox.getChildren().add(card);
            vBoxs.add(card);
        }
        return vBoxs;
    }

    /**
     * Adds a new list to the workspace. Used when loading.
     */
    public ListView<Map<String,String>> addNewList() {
        ListView<Map<String,String>> obsList = new ListView<>();

        addContextMenu(obsList);
        addCellFactory(obsList);
        
        workspace.add(obsList);
        return obsList;
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
                        deleteItem(obsList);
                    }
                });
                MenuItem editMenuItem = new MenuItem("Edit");
                editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Edit Item");
                        editItem(obsList);
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

    /**
     * Removes the selected item in the ObservableList. It will first ask the user if they are sure
     * they want to remove the item. If OK, then the item is removed from the underlying workspace
     * and then the ObservableList itself.
     *
     * @param   obsList The observable list that contains the item to be deleted
     */
    private void deleteItem(final ListView<Map<String,String>> obsList) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delelte Item");
        Map<String, String> item = obsList.getSelectionModel().getSelectedItem();
        alert.setHeaderText("Delete item: " + item.get("Name"));
        alert.setContentText("Are you sure? Press OK to confirm or Cancel to exit.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            WorkspaceManager.getInstance().stepIntoWorkspace(workspace.indexOf(obsList));
            WorkspaceManager.getInstance().deleteWorkspace(obsList.getItems().indexOf(item));
            WorkspaceManager.getInstance().stepUp();
            obsList.getItems().remove(item);
        }
    }
    /**
     * Edits the selected item in the ObservableList. A diablog will popup, which wiill be
     * populated with the items current details, which the user can edit. It then saves these
     * changes in the underlying workspace and ObservabelList.
     *
     * @param   obsList The observable list that contains the item to be edited
     */
    private void editItem(final ListView<Map<String,String>> obsList) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Item");
        dialog.setHeaderText("Use this dialog to edit item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("resources/NewNodeDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Unable to load dialog");
            e.printStackTrace();
            return;
        }

        Map<String, String> item = obsList.getSelectionModel().getSelectedItem();
        NewNodeDialogController controller = fxmlLoader.getController();
        controller.setNewNodeDesc(item.get("Description")).setNewNodeName(item.get("Name")).setNewNodeProirity(item.get("Priority"));

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Map<String, String> newItem = controller.processInputs();
            item.put("Name", newItem.get("Name"));
            item.put("Description", newItem.get("Description"));
            item.put("Priority", newItem.get("Priority"));

            obsList.edit(obsList.getSelectionModel().getSelectedIndex());

            replaceItem(obsList, item, newItem);
        } else {
            System.out.println("Cancel edit");
        }
    }
    /**
     * Given a list of items and an item in the list, it will replace that item with the new item.
     *
     * @param obsList   List that contains item to be replaced
     * @param item      Item to repace
     * @param newItem   The item to take its place
     */
    private void replaceItem(final ListView<Map<String, String>> obsList, Map<String, String> item, Map<String, String> newItem) {
        WorkspaceManager.getInstance().stepIntoWorkspace(workspace.indexOf(obsList));
        WorkspaceManager.getInstance().stepIntoWorkspace(workspace.get(workspace.indexOf(obsList)).getItems().indexOf(item));
        WorkspaceManager.getInstance().setName(newItem.get("Name"));
        WorkspaceManager.getInstance().setDescription(newItem.get("Description"));
        WorkspaceManager.getInstance().setPriority(Integer.parseInt(newItem.get("Priority")));
        WorkspaceManager.getInstance().stepUp();
        WorkspaceManager.getInstance().stepUp();
        int index = workspace.get(workspace.indexOf(obsList)).getItems().indexOf(item);
        workspace.get(workspace.indexOf(obsList)).getItems().remove(index);
        workspace.get(workspace.indexOf(obsList)).getItems().add(index, newItem);
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
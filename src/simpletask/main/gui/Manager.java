package simpletask.main.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import simpletask.main.app.TestGUI;
import simpletask.main.entities.NodeData;
import simpletask.main.entities.NodeKeys;
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
    private List<ListView<NodeData>> workspace;

    private static final Manager manager = new Manager();

    private DataFormat df = new DataFormat("WorkspaceNode");
    private DataFormat position = new DataFormat("Position");

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
    public void addList(final ListView<NodeData> obsList) {
        workspace.add(obsList);
    }

    /**
     * Adds the element to the ObservableList in the workspace.
     *
     * @param element   Element to add to the ObservableList
     * @param obsList   The list to add to
     */
    public void addToList(final ListView<NodeData> obsList, final NodeData element) {
        workspace.get(workspace.indexOf(obsList)).getItems().add(element);
    }

    public ArrayList<VBox> loadWorkspace() {
        workspace.clear();
        ArrayList<VBox> vBoxs = new ArrayList<>();
        for (NodeData w: WorkspaceManager.getInstance().getTasks()) {
            ListView<NodeData> newList = Manager.getInstance().addNewList();
            VBox card;
            try {
                card = FXMLLoader.load(getClass().getResource("resources/Card.fxml"));
            } catch (IOException e) {
                System.err.println("Unable to load FXML");
                e.printStackTrace();
                return null;
            }
            ((Text) card.getChildren().get(0)).setText(w.getAttr(NodeKeys.NAME));
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
    public ListView<NodeData> addNewList() {
        ListView<NodeData> obsList = new ListView<>();

        addContextMenu(obsList);
        addCellFactory(obsList);
        
        workspace.add(obsList);
        return obsList;
    }

    public ListView<NodeData> addToWorkspace(final String name) {
        WorkspaceManager.getInstance().addWorkspace(name, "Task");
        ListView<NodeData> obsList = new ListView<>();

        // TODO: Needs to be handled better. Ideally set in one place with options blurred out if they are invalid
        addContextMenu(obsList);
        addCellFactory(obsList);
        
        workspace.add(obsList);
        return obsList;
    }

    private void addContextMenu(final ListView<NodeData> obsList) {
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

    private void addCellFactory(final ListView<NodeData> obsList) {
        obsList.setCellFactory(new Callback<ListView<NodeData>,ListCell<NodeData>>(){
            @Override
            public ListCell<NodeData> call(ListView<NodeData> param) {
                ListCell<NodeData> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(NodeData item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getAttr(NodeKeys.NAME));
                        }
                    }

                    @Override
                    public void startEdit() {
                        setText(getListView().getSelectionModel().getSelectedItem().getAttr(NodeKeys.NAME));
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
                // Double click logic
                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                            if(mouseEvent.getClickCount() == 2){
                                // Move into workspace containing this cell
                                int index = workspace.indexOf(cell.getListView());
                                System.out.println(WorkspaceManager.getInstance().getTasks().get(index));
                                System.out.println(index);
                                WorkspaceManager.getInstance().stepIntoWorkspace(index);
                                // Redraw scene
                                FXMLLoader mainWorkspace = new FXMLLoader(getClass().getResource("resources/Workspace.fxml"));
                                try {
                                    Parent root = mainWorkspace.load();
                                    TestGUI.getStage().setScene(new Scene(root, 900, 500));
                                    
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                
                // Drag and Drop Logic
                //TODO: This is working but really needs tidying...
                cell.setOnDragDetected(new EventHandler <MouseEvent>() {
                    public void handle(MouseEvent event) {
                        /* drag was detected, start drag-and-drop gesture*/
                        System.out.println("onDragDetected");
                        
                        /* allow any transfer mode */
                        Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                        
                        /* put a string on dragboard */
                        ClipboardContent content = new ClipboardContent();
                        
                        content.put(df, cell.getItem());
                        content.put(position, workspace.indexOf(cell.getListView()));
                        content.putString(cell.getText());
                        db.setContent(content);
                        
                        event.consume();
                    }
                });
        
                cell.setOnDragOver(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* data is dragged over the target */
                        System.out.println("onDragOver");
                        
                        // /* accept it only if it is  not dragged from the same node 
                        //  * and if it has a string data */
                        if (event.getDragboard().hasString()) {
                            /* allow for both copying and moving, whatever user chooses */
                            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        }
                        
                        event.consume();
                    }
                });
        
                cell.setOnDragEntered(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* the drag-and-drop gesture entered the target */
                        System.out.println("onDragEntered");
                        /* show to the user that it is an actual gesture target */
                        // if (event.getGestureSource() != target &&
                        //         event.getDragboard().hasString()) {
                        //     target.setFill(Color.GREEN);
                        // }
                        
                        event.consume();
                    }
                });
        
                cell.setOnDragExited(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        System.out.println("onDragExited");
                        /* mouse moved away, remove the graphical cues */
                        // target.setFill(Color.BLACK);
                        
                        event.consume();
                    }
                });
                
                cell.setOnDragDropped(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* data dropped */
                        System.out.println("onDragDropped");
                        /* if there is a string data on dragboard, read it and use it */
                        Dragboard db = event.getDragboard();
                        boolean success = false;
                        if (db.hasString()) {
                            ArrayList<Integer> path = new ArrayList<>();
                            path.add(workspace.indexOf(cell.getListView()));
                            cell.getListView().getItems().add((NodeData) db.getContent(df));
                            WorkspaceManager.getInstance().stepIntoWorkspace((Integer) db.getContent(position));
                            WorkspaceManager.getInstance().stepIntoWorkspace(workspace.get((Integer) db.getContent(position)).getItems().indexOf((NodeData) db.getContent(df)));
                            WorkspaceManager.getInstance().moveCurrentWorkspace(path);
                            WorkspaceManager.getInstance().stepUp();
                            WorkspaceManager.getInstance().stepUp();
                            success = true;
                        }
                        /* let the source know whether the string was successfully 
                         * transferred and used */
                        event.setDropCompleted(success);
                        
                        event.consume();
                    }
                });
        
                cell.setOnDragDone(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* the drag-and-drop gesture ended */
                        System.out.println("onDragDone");
                        Dragboard db = event.getDragboard();
                        /* if the data was successfully moved, clear it */
                        if (event.getTransferMode() == TransferMode.MOVE) {
                            // int pos2 = cell.getListView().getItems().indexOf((NodeData) db.getContent(df));
                            cell.getListView().getItems().remove((NodeData) db.getContent(df));
                            // int pos = workspace.indexOf(cell.getListView());
                            // WorkspaceManager.getInstance().stepIntoWorkspace(pos);
                            // WorkspaceManager.getInstance().deleteWorkspace(pos2);
                            // WorkspaceManager.getInstance().stepUp();
                            db.clear();
                        }
                        
                        event.consume();
                    }
                });
                
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
    private void deleteItem(final ListView<NodeData> obsList) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delelte Item");
        NodeData item = obsList.getSelectionModel().getSelectedItem();
        alert.setHeaderText("Delete item: " + item.getAttr(NodeKeys.NAME));
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
     * Edits the selected item in the ObservableList. A diablog will popup, which will be
     * populated with the items current details, which the user can edit. It then saves these
     * changes in the underlying workspace and ObservabelList.
     *
     * @param   obsList The observable list that contains the item to be edited
     */
    private void editItem(final ListView<NodeData> obsList) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setResizable(true);
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
        String numOfTasks = obsList.getItems().get(obsList.getSelectionModel().getSelectedIndex()).getAttr(NodeKeys.TASKS);
        if (!numOfTasks.equals("0")) {
            // Note, this relies on the thrid last element being the combo list
            ObservableList<Node> temp = ((GridPane) ((DialogPane) dialog.getDialogPane().getChildren().get(3)).getChildren().get(3)).getChildren();
            ((ComboBox<String>) temp.get(temp.size() - 5)).setDisable(true);
        }

        NodeData item = obsList.getSelectionModel().getSelectedItem();
        NewNodeDialogController controller = fxmlLoader.getController();
        controller.setNewNodeDesc(item.getAttr(NodeKeys.DESCRIPTION)).setNewNodeName(item.getAttr(NodeKeys.NAME)).setNewNodeProirity(item.getAttr(NodeKeys.PRIORITY))
            .setNewNodeDueDate(item.getAttr(NodeKeys.DUEDATE)).setNewNodeType(item.getAttr(NodeKeys.TYPE)).setNewNodeComplete(item.getAttr(NodeKeys.COMPLETE))
            .setNewNodeTasks(numOfTasks);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NodeData newItem = controller.processInputs();

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
    private void replaceItem(final ListView<NodeData> obsList, NodeData item, NodeData newItem) {
        WorkspaceManager.getInstance().stepIntoWorkspace(workspace.indexOf(obsList));
        WorkspaceManager.getInstance().stepIntoWorkspace(workspace.get(workspace.indexOf(obsList)).getItems().indexOf(item));
        WorkspaceManager.getInstance().setName(newItem.getAttr(NodeKeys.NAME));
        WorkspaceManager.getInstance().setDescription(newItem.getAttr(NodeKeys.DESCRIPTION));
        // Don't change if unsucessful
        if (!WorkspaceManager.getInstance().setPriority(newItem.getAttr(NodeKeys.PRIORITY))) {
            newItem.setAttr(NodeKeys.PRIORITY, item.getAttr(NodeKeys.PRIORITY));
        };
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
    private void addItemToList(final ListView<NodeData> obsList, final ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setResizable(true);
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
            NodeData newItem = controller.processInputs();
            // TODO: Need to create a DateTime Picker in JavaFX to get rid of this
            newItem.setAttr(NodeKeys.DUEDATE, newItem.getAttr(NodeKeys.DUEDATE) + "T00:00:00.000000000");
            WorkspaceManager.getInstance().stepIntoWorkspace(workspace.indexOf(obsList));
            WorkspaceManager.getInstance().addWorkspace(newItem);
            WorkspaceManager.getInstance().stepUp();
            addToList(workspace.get(workspace.indexOf(obsList)), newItem);
        } else {
            System.out.println("Cancel pressed");
        }
    }
}
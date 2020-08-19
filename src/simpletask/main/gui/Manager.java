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
import simpletask.main.app.AppGUI;
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
 * <p>
 * Since the Manager is only a view of the workspace, only the top three levels of
 * the current workspace are shown. i.e. the name of the workspace, it's subtasks and
 * the subtasks subtasks.
 *
 * @author Matthew Taggart
 */
public final class Manager {
    /**
     * A list of subtasks for the current workspace. Each item in this list is itself
     * a list of subtasks.
     */
    @FXML
    private List<ListView<NodeData>> workspace;
    /**
     * The sole Manager instance.
     */
    private static final Manager MANAGER = new Manager();
    /**
     * DataFormat used to store the node data of a cell during a drag and drop.
     */
    private DataFormat df = new DataFormat("WorkspaceNode");
    /**
     * DataFormat used to store the position of a cell during a drag and drop. The
     * position is needed the Manager knows which list to drop the cell into in the
     * workspace.
     */
    private DataFormat position = new DataFormat("Position");
    /**
     * Data used to populate the summary section for the selected task's parent.
     */
    private NodeData mainTaskData = null;
    /**
     * Data used to populate the summary section for the selected task.
     */
    private NodeData subTaskData = null;
    /**
     * Minimum width of a card, in px.
     */
    private final int minCardWidth = 200;
    /**
     * Maximum height of a card, in px.
     */
    private final int minCardHeight = 300;
    /**
     * Index in edit dialog of the dialog pane.
     */
    private final int dialogPaneIndex = 3;
    /**
     * Index in edit dialog of the grid pane.
     */
    private final int gridPaneIndex = 3;
    /**
     * Index in edit dialog of the combo box.
     */
    private final int comboDropDown = 5;
    /**
     * Private no-args constructor. Ensures that the workspace is correctly initialised.
     */
    private Manager() {
        workspace = new ArrayList<>();
    };
    /**
     * Gets the instance of this singleton.
     *
     * @return  The instance of Manager
     */
    public static Manager getInstance() {
        return MANAGER;
    }
    /**
     * Getter for mainTaskData.
     *
     * @return  mainTaskData
     */
    public NodeData getMainTaskData() {
        return mainTaskData;
    }
    /**
     * Getter for subTaskData.
     *
     * @return  subTaskData
     */
    public NodeData getSubTaskData() {
        return subTaskData;
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
    /**
     * Sets up the workspace based off the current Workspace in WorkspaceManager. For each
     * subtask in the WorkspaceManager, a card is created (based of the Card.fxml). This card
     * contains a list of that tasks subtasks.
     * <p>
     * It will then set the title of each card to the name of the task.
     *
     * @return  A list for VBoxes representing the cards
     */
    public ArrayList<VBoxWrapper> loadWorkspace() {
        workspace.clear();
        ArrayList<VBoxWrapper> vBoxs = new ArrayList<>();
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
            card.getChildren().remove(1);   // Removes the empty ListView from the card
            //TODO: Try to get list view to grow
            VBox.setVgrow(newList, Priority.ALWAYS);
            newList.setMaxSize(minCardWidth, Double.MAX_VALUE);
            newList.setMinSize(minCardWidth, minCardHeight);
            card.getChildren().add(newList);
            vBoxs.add(new VBoxWrapper(card, newList));
        }
        return vBoxs;
    }
    /**
     * Adds a new empty list to the workspace.
     *
     * @return  The list of NodeData that is added to workspace.
     */
    private ListView<NodeData> addNewList() {
        ListView<NodeData> obsList = new ListView<>();

        addContextMenu(obsList);
        addCellFactory(obsList);

        workspace.add(obsList);
        return obsList;
    }
    /**
     * Adds a Task node to the WorkspaceManager and an empty card to represent
     * the new node.
     *
     * @param name  Name of the new workspace
     * @return      The list of NodeData representing the new node
     */
    public ListView<NodeData> addToWorkspace(final String name) {
        WorkspaceManager.getInstance().addWorkspace(name, "Task");
        ListView<NodeData> obsList = new ListView<>();

        // TODO: Needs to be handled better. Ideally set in one place with options blurred out if they are invalid
        addContextMenu(obsList);
        addCellFactory(obsList);

        workspace.add(obsList);
        return obsList;
    }
    /**
     * Adds a context menu to a ListView that is representing a list of tasks. The menu
     * contains an option to edit, delete or add to the list view.
     * <p>
     * Currently, only the add and delete works.
     *
     * @param obsList   The ListView to add the context menu to
     */
    private void addContextMenu(final ListView<NodeData> obsList) {
        ContextMenu listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                deleteList(obsList);
                System.out.println("Delete");
            }
        });
        MenuItem editMenuItem = new MenuItem("Edit");
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                System.out.println("Edit");
            }
        });
        MenuItem addMenuItem = new MenuItem("Add");
        addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                System.out.println("Add");
                addItemToList(obsList, event);
            }
        });
        listContextMenu.getItems().addAll(addMenuItem, deleteMenuItem, editMenuItem);
        obsList.setContextMenu(listContextMenu);
    }
    /**
     * Addes a cell factory to the inputted obsList. Each cell will displayed using the name
     * of the tasks it represents. They will also all have their own context menu to add, delete
     * and edit cells.
     * <p>
     * On double click, you will move into the cells parent, as in you will move your current
     * workspace. There is also drag and drop logic used to move cells between lists.
     *
     * @param obsList   The list to add the cell factory to
     */
    private void addCellFactory(final ListView<NodeData> obsList) {
        obsList.setCellFactory(new Callback<ListView<NodeData>, ListCell<NodeData>>() {
            @Override
            public ListCell<NodeData> call(final ListView<NodeData> param) {
                ListCell<NodeData> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(final NodeData item, final boolean empty) {
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
                deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent event) {
                        System.out.println("Delete Item");
                        deleteItem(obsList);
                    }
                });
                MenuItem editMenuItem = new MenuItem("Edit");
                editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent event) {
                        System.out.println("Edit Item");
                        editItem(obsList);
                    }
                });
                MenuItem addMenuItem = new MenuItem("Add");
                addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent event) {
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
                addDoubleClickLogic(cell);
                // Drag and Drop Logic
                addDragAndDrop(cell);
                return cell;
            }
        });
    }
    /**
     * Adds drag and drop logic to a cell. A cell can be dragged and dropped between
     * lists. This allows the user to move tasks around, changing their parent.
     * <p>
     * Two things are copied into the clipboard during the drag and drop. The first is the
     * actual node data, this is to know which node to move. The second is the initial
     * position of that node, this is to make it easier to find and move the node.
     *
     * @param cell  The cell to add the logic to
     */
    private void addDragAndDrop(final ListCell<NodeData> cell) {
        cell.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(final MouseEvent event) {
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

        cell.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(final DragEvent event) {
                /* data is dragged over the target */
                System.out.println("onDragOver");

                // accept it only if it is  not dragged from the same node
                // and if it has a string data
                if (event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                event.consume();
            }
        });

        cell.setOnDragEntered(new EventHandler<DragEvent>() {
            public void handle(final DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                System.out.println("onDragEntered");
                event.consume();
            }
        });

        cell.setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(final DragEvent event) {
                System.out.println("onDragExited");
                event.consume();
            }
        });

        cell.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(final DragEvent event) {
                /* data dropped */
                System.out.println("onDragDropped");
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    ArrayList<Integer> path = new ArrayList<>(WorkspaceManager.getInstance().getPath());
                    path.add(workspace.indexOf(cell.getListView()));
                    cell.getListView().getItems().add((NodeData) db.getContent(df));
                    WorkspaceManager.getInstance().stepIntoWorkspace((Integer) db.getContent(position));
                    WorkspaceManager.getInstance().stepIntoWorkspace(workspace.get((Integer) db.getContent(position))
                                                                                    .getItems().indexOf((NodeData) db.getContent(df)));
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

        cell.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(final DragEvent event) {
                /* the drag-and-drop gesture ended */
                System.out.println("onDragDone");
                Dragboard db = event.getDragboard();
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
                    cell.getListView().getItems().remove((NodeData) db.getContent(df));
                    db.clear();
                }
                event.consume();
            }
        });
    }
    /**
     * Adds click logic to the cell.
     * <p>
     * If there is only a single click, then the summary tabs are populated with the
     * mainTaskData and subTaskData.
     * <p>
     * If it is a double click, the current workspace is moved to the parent task of the
     * cell. The GUI is then redrawn.
     *
     * @param cell  The cell to add the logic to
     */
    private void addDoubleClickLogic(final ListCell<NodeData> cell) {
        cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (cell.getIndex() < cell.getListView().getItems().size()) {
                        // Get node and parent of node (in the form of NodeData)
                        int mainTaskIndex = workspace.indexOf(cell.getListView());
                        ArrayList<Integer> path = new ArrayList<>();
                        path.add(mainTaskIndex);
                        mainTaskData = WorkspaceManager.getInstance().relativeDetailsOf(path);
                        path = new ArrayList<>();
                        path.add(mainTaskIndex);
                        path.add(cell.getIndex());
                        subTaskData = WorkspaceManager.getInstance().relativeDetailsOf(path);
                        if (mouseEvent.getClickCount() == 2) {
                            // Move into workspace containing this cell
                            int index = workspace.indexOf(cell.getListView());
                            WorkspaceManager.getInstance().stepIntoWorkspace(index);
                            // Redraw scene
                            FXMLLoader mainWorkspace = new FXMLLoader(getClass().getResource("resources/Workspace.fxml"));
                            try {
                                Parent root = mainWorkspace.load();
                                AppGUI.getStage().setScene(new Scene(root, AppGUI.WIDTH, AppGUI.LENGTH));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
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
     * Edits the selected item in the ObservableList. A dialog will popup, which will be
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
            ObservableList<Node> temp = ((GridPane) ((DialogPane) dialog.getDialogPane()
                                                                 .getChildren().get(dialogPaneIndex)).getChildren().get(gridPaneIndex)).getChildren();
            temp.get(temp.size() - comboDropDown).setDisable(true);
        }

        NodeData item = obsList.getSelectionModel().getSelectedItem();
        NewNodeDialogController controller = fxmlLoader.getController();
        controller.setNewNodeDesc(item.getAttr(NodeKeys.DESCRIPTION)).setNewNodeName(item.getAttr(NodeKeys.NAME))
            .setNewNodeProirity(item.getAttr(NodeKeys.PRIORITY)).setNewNodeDueDate(item.getAttr(NodeKeys.DUEDATE))
            .setNewNodeType(item.getAttr(NodeKeys.TYPE)).setNewNodeComplete(item.getAttr(NodeKeys.COMPLETE)).setNewNodeTasks(numOfTasks);

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
    private void replaceItem(final ListView<NodeData> obsList, final NodeData item, final NodeData newItem) {
        WorkspaceManager.getInstance().stepIntoWorkspace(workspace.indexOf(obsList));
        WorkspaceManager.getInstance().stepIntoWorkspace(workspace.get(workspace.indexOf(obsList)).getItems().indexOf(item));
        WorkspaceManager.getInstance().setName(newItem.getAttr(NodeKeys.NAME));
        WorkspaceManager.getInstance().setDescription(newItem.getAttr(NodeKeys.DESCRIPTION));
        // Don't change if unsucessful
        if (!WorkspaceManager.getInstance().setPriority(newItem.getAttr(NodeKeys.PRIORITY))) {
            newItem.setAttr(NodeKeys.PRIORITY, item.getAttr(NodeKeys.PRIORITY));
        }
        WorkspaceManager.getInstance().setComplete(newItem.getAttr(NodeKeys.COMPLETE));
        WorkspaceManager.getInstance().setType(newItem.getAttr(NodeKeys.TYPE));
        WorkspaceManager.getInstance().setDueDate(newItem.getAttr(NodeKeys.DUEDATE));
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
     * @param obsList   The list to add the item to
     * @param event     Event that triggers this method
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
    /**
     * Used to delete a card from the GUI. Triggered through the obsList context menu.
     * <p>
     * At first, a dialog pops up asking the user if they are sure they want to delete the task.
     * If so then the task is first removed from the WorkspaceManager then from the Manager. The
     * GUI is then redrawn to reflect this change.
     *
     * @param obsList   The list to delete
     */
    private void deleteList(final ListView<NodeData> obsList) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setResizable(true);
        dialog.setTitle("Delete Item");
        String name = WorkspaceManager.getInstance().getTasks().get(workspace.indexOf(obsList)).getAttr(NodeKeys.NAME);
        dialog.setHeaderText("Do you want to delete " + name + "?");

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            WorkspaceManager.getInstance().deleteWorkspace(workspace.indexOf(obsList));
            workspace.remove(obsList);
            // Redraw scene
            FXMLLoader mainWorkspace = new FXMLLoader(getClass().getResource("resources/Workspace.fxml"));
            try {
                Parent root = mainWorkspace.load();
                AppGUI.getStage().setScene(new Scene(root, AppGUI.WIDTH, AppGUI.LENGTH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Cancel pressed");
        }
    }
    /**
     * Replaces the data at index with the item. It assumes that you are editing through the
     * GUI, so the item you are editing is two levels down. As a result, it will only try to
     * edit those that are 2 levels below the current.
     *
     * @param index Path to node to replace
     * @param item  Node to replace it with
     */
    public void editAtIndex(final int[] index, final NodeData item) {
        ListView<NodeData> obsList = workspace.get(index[0]);
        if (index.length == 2) {
            replaceItem(obsList, obsList.getItems().get(index[1]), item);
        } else {
            System.err.println("NYI");
        }
    }
}

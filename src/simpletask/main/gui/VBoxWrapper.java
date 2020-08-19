package simpletask.main.gui;

import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import simpletask.main.entities.NodeData;

/**
 * Class used as a wrapper for a VBox. It is used to exposet the ObservabeleList
 * to other classes so there are no unsafe casts happening.
 */
public class VBoxWrapper {
    /**
     * Holds the VBox.
     */
    private VBox vBox;
    /**
     * Holds the ListView.
     */
    private ListView<NodeData> list;
    /**
     * No args constructor to set up the VBox and ListView.
     */
    public VBoxWrapper() {
        vBox = new VBox();
        list = new ListView<>();
    }
    /**
     * Constructs a wrapper given a VBox and ListView.
     *
     * @param vBox  The Vbox to wrap
     * @param list  The ListView to wrap
     */
    public VBoxWrapper(final VBox vBox, final ListView<NodeData> list) {
        this.vBox = vBox;
        this.list = list;
    }
    /**
     * Getter for vbox.
     *
     * @return  The wrapped VBox
     */
    public VBox getVBox() {
        return vBox;
    }
    /**
     * Getter for the list.
     *
     * @return  The list
     */
    public ListView<NodeData> getListView() {
        return list;
    }
}

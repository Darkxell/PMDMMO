package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.DungeonItemGroup;
import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.item.Item;

import fr.darkxell.dataeditor.application.data.DungeonItemTableItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class EditDungeonItemsController implements Initializable {

    public static final byte GROUND = 0, BURIED = 1, SHOP = 2;
    public static EditDungeonItemsController instance;

    @FXML
    public HBox container;
    @FXML
    public MenuItem createMenu;
    @FXML
    public MenuItem deleteMenu;
    @FXML
    public EditItemController editItemController;
    @FXML
    public TitledPane editItemPane;
    @FXML
    public MenuItem editMenu;
    @FXML
    public TableColumn<DungeonItemTableItem, FloorSet> floorsColumn;
    @FXML
    public TableColumn<DungeonItemTableItem, Item[]> itemsColumn;
    @FXML
    public TableView<DungeonItemTableItem> itemTable;
    public byte itemType = GROUND;
    @FXML
    public TableColumn<DungeonItemTableItem, Integer> weightsColumn;

    public ArrayList<DungeonItemGroup> generate() {
        ArrayList<DungeonItemGroup> a = new ArrayList<>();
        for (DungeonItemTableItem i : this.itemTable.getItems())
            a.add(i.itemGroup);
        return a;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        this.editItemPane.setVisible(false);
        this.itemsColumn.setCellValueFactory(new PropertyValueFactory<>("items"));
        this.weightsColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.floorsColumn.setCellValueFactory(new PropertyValueFactory<>("floors"));
    }

    public void onCreate() {
        DungeonItemTableItem i = new DungeonItemTableItem(
                new DungeonItemGroup(new FloorSet(1, EditDungeonDataController.instance.currentFloorCount()), 1,
                        new Integer[] { Item.POKEDOLLARS }, new Integer[] { 1 }));
        this.itemTable.getItems().add(i);
        this.itemTable.getSelectionModel().select(i);
        this.onEdit();
    }

    public void onDelete() {
        this.itemTable.getItems().remove(this.itemTable.getSelectionModel().getSelectedItem());
    }

    public void onEdit() {
        this.itemTable.getItems().sort(Comparator.naturalOrder());
        this.editItemPane.setVisible(this.itemTable.getSelectionModel().getSelectedItem() != null);
        this.editItemController.setupFor(this.itemTable.getSelectionModel().getSelectedItem());
    }

    public void onItemEdited(DungeonItemTableItem item) {
        this.itemTable.refresh();
    }

    public void onMenuRequested() {
        this.editMenu.setDisable(this.itemTable.getSelectionModel().isEmpty());
        this.deleteMenu.setDisable(this.itemTable.getSelectionModel().isEmpty());
    }

    public void setupFor(Dungeon dungeon) {
        this.itemTable.getItems().clear();
        ArrayList<DungeonItemGroup> w;
        if (this.itemType == BURIED)
            w = dungeon.buriedItemsData();
        else if (this.itemType == SHOP)
            w = dungeon.shopItemsData();
        else
            w = dungeon.itemsData();
        for (DungeonItemGroup e : w)
            this.itemTable.getItems().add(new DungeonItemTableItem(e));
        this.onEdit();
    }

}

package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.model.dungeon.DungeonTrapGroupModel;
import com.darkxell.common.trap.Trap;
import com.darkxell.common.trap.TrapRegistry;

import fr.darkxell.dataeditor.application.data.DungeonTrapTableItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class EditDungeonTrapsController implements Initializable {

    public static EditDungeonTrapsController instance;

    @FXML
    public HBox container;
    @FXML
    public MenuItem createMenu;
    @FXML
    public MenuItem deleteMenu;
    @FXML
    public MenuItem editMenu;
    @FXML
    public EditTrapController editTrapController;
    @FXML
    public TitledPane editTrapPane;
    @FXML
    public TableColumn<DungeonTrapTableItem, FloorSet> floorsColumn;
    @FXML
    public TableColumn<DungeonTrapTableItem, Trap[]> trapsColumn;
    @FXML
    public TableView<DungeonTrapTableItem> trapTable;
    @FXML
    public TableColumn<DungeonTrapTableItem, Integer[]> weightsColumn;

    public ArrayList<DungeonTrapGroupModel> generate() {
        ArrayList<DungeonTrapGroupModel> a = new ArrayList<>();
        for (DungeonTrapTableItem i : this.trapTable.getItems())
            a.add(i.trapGroup);
        return a;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        this.editTrapPane.setVisible(false);
        this.trapsColumn.setCellValueFactory(new PropertyValueFactory<>("traps"));
        this.weightsColumn.setCellValueFactory(new PropertyValueFactory<>("weights"));
        this.floorsColumn.setCellValueFactory(new PropertyValueFactory<>("floors"));
    }

    public void onCreate() {
        DungeonTrapTableItem i = new DungeonTrapTableItem(
                new DungeonTrapGroupModel(new Integer[] { TrapRegistry.WONDER_TILE.id }, new Integer[] { 1 },
                        new FloorSet(1, EditDungeonDataController.instance.currentFloorCount())));
        this.trapTable.getItems().add(i);
        this.trapTable.getSelectionModel().select(i);
        this.onEdit();
    }

    public void onDelete() {
        this.trapTable.getItems().remove(this.trapTable.getSelectionModel().getSelectedItem());
    }

    public void onEdit() {
        this.trapTable.getItems().sort(Comparator.naturalOrder());
        this.editTrapPane.setVisible(this.trapTable.getSelectionModel().getSelectedItem() != null);
        this.editTrapController.setupFor(this.trapTable.getSelectionModel().getSelectedItem());
    }

    public void onMenuRequested() {
        this.editMenu.setDisable(this.trapTable.getSelectionModel().isEmpty());
        this.deleteMenu.setDisable(this.trapTable.getSelectionModel().isEmpty());
    }

    public void onTrapEdited(DungeonTrapTableItem item) {
        this.trapTable.refresh();
    }

    public void setupFor(Dungeon dungeon) {
        this.trapTable.getItems().clear();
        ArrayList<DungeonTrapGroupModel> w = dungeon.trapsData();
        for (DungeonTrapGroupModel e : w)
            this.trapTable.getItems().add(new DungeonTrapTableItem(e));
        this.onEdit();
    }

}

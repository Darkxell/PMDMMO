package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.darkxell.common.dungeon.data.DungeonTrapGroup;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.trap.Trap;
import com.darkxell.common.trap.TrapRegistry;

import fr.darkxell.dataeditor.application.data.DungeonTrapTableItem;
import fr.darkxell.dataeditor.application.data.SingleTrapTableItem;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class EditTrapController implements Initializable {

    public DungeonTrapTableItem editing;

    @FXML
    public EditFloorsetController floorsetController;
    @FXML
    public MenuItem removeMenuItem;
    @FXML
    public TableColumn<SingleTrapTableItem, Trap> trapColumn;
    @FXML
    public TableView<SingleTrapTableItem> trapsTable;
    @FXML
    public TableColumn<SingleTrapTableItem, String> weightColumn;

    private DungeonTrapGroup generate() {
        ObservableList<SingleTrapTableItem> list = this.trapsTable.getItems();
        int[] ids = new int[list.size()], chances = new int[list.size()];
        for (int i = 0; i < ids.length; ++i) {
            ids[i] = list.get(i).trap.id;
            chances[i] = list.get(i).weight;
        }
        return new DungeonTrapGroup(ids, chances, this.floorsetController.generate());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.trapColumn.setCellValueFactory(new PropertyValueFactory<>("trap"));
        this.weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

        ArrayList<Trap> traps = Registries.traps().toList();
        Trap[] t = new Trap[traps.size()];
        for (int i = 0; i < t.length; ++i)
            t[i] = traps.get(i);
        this.trapColumn.setCellFactory(ComboBoxTableCell.forTableColumn(t));
        this.weightColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void onAdd() {
        this.trapsTable.getItems().add(new SingleTrapTableItem(TrapRegistry.WONDER_TILE, 1));
    }

    public void onMenuShown() {
        this.removeMenuItem.setDisable(this.trapsTable.getSelectionModel().getSelectedItem() == null);
    }

    public void onRemove() {
        if (this.trapsTable.getSelectionModel().getSelectedItem() != null)
            this.trapsTable.getItems().remove(this.trapsTable.getSelectionModel().getSelectedItem());
    }

    public void onSave() {
        this.editing.trapGroup = this.generate();
        EditDungeonTrapsController.instance.onTrapEdited(this.editing);
    }

    public void onTrapEdited(CellEditEvent<SingleTrapTableItem, Trap> cell) {
        cell.getRowValue().trap = cell.getNewValue();
    }

    public void onWeightEdited(CellEditEvent<SingleTrapTableItem, String> cell) {
        String input = cell.getNewValue();
        if (input.matches("\\d+"))
            cell.getRowValue().weight = Integer.parseInt(input);
        else {
            FXUtils.showAlert("Wrong Weight value: " + input + ". Must be an integer.");
            this.trapsTable.refresh();
        }
    }

    public void setupFor(DungeonTrapTableItem item) {
        this.editing = item;
        this.trapsTable.getItems().clear();
        if (item != null) {
            for (int i = 0; i < item.trapGroup.ids.length; ++i)
                this.trapsTable.getItems().add(new SingleTrapTableItem(Registries.traps().find(item.trapGroup.ids[i]),
                        item.trapGroup.chances[i]));
            this.floorsetController.setupFor(item.getFloors());
        }
    }

}

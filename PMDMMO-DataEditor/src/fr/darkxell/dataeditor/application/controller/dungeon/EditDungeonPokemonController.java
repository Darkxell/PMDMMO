package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.DungeonEncounter;
import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.model.dungeon.DungeonEncounterModel;
import com.darkxell.common.weather.Weather;

import fr.darkxell.dataeditor.application.data.DungeonEncounterTableItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class EditDungeonPokemonController implements Initializable {

    public static EditDungeonPokemonController instance;

    @FXML
    public HBox container;
    @FXML
    public MenuItem createMenu;
    @FXML
    public MenuItem deleteMenu;
    @FXML
    public MenuItem editMenu;
    @FXML
    public EditEncounterController editPokemonController;
    @FXML
    public TitledPane editPokemonPane;
    @FXML
    public TableColumn<DungeonEncounterTableItem, FloorSet> floorsColumn;
    @FXML
    public TableColumn<DungeonEncounterTableItem, Integer> levelColumn;
    @FXML
    public TableColumn<DungeonEncounterTableItem, Weather> pokemonColumn;
    @FXML
    public TableView<DungeonEncounterTableItem> pokemonTable;
    @FXML
    public TableColumn<DungeonEncounterTableItem, Integer> weightColumn;

    public ArrayList<DungeonEncounterModel> generate() {
        ArrayList<DungeonEncounterModel> a = new ArrayList<>();
        for (DungeonEncounterTableItem i : this.pokemonTable.getItems())
            a.add(i.encounter);
        return a;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        this.editPokemonPane.setVisible(false);
        this.pokemonColumn.setCellValueFactory(new PropertyValueFactory<>("pokemon"));
        this.levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        this.weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.floorsColumn.setCellValueFactory(new PropertyValueFactory<>("floors"));
    }

    public void onCreate() {
        DungeonEncounterTableItem i = new DungeonEncounterTableItem(new DungeonEncounterModel(1, 1, 1, null,
                new FloorSet(1, EditDungeonDataController.instance.currentFloorCount())));
        this.pokemonTable.getItems().add(i);
        this.pokemonTable.getSelectionModel().select(i);
        this.onEdit();
    }

    public void onDelete() {
        this.pokemonTable.getItems().remove(this.pokemonTable.getSelectionModel().getSelectedItem());
    }

    public void onEdit() {
        this.pokemonTable.getItems().sort(Comparator.naturalOrder());
        this.editPokemonPane.setVisible(this.pokemonTable.getSelectionModel().getSelectedItem() != null);
        this.editPokemonController.setupFor(this.pokemonTable.getSelectionModel().getSelectedItem());
    }

    public void onMenuRequested() {
        this.editMenu.setDisable(this.pokemonTable.getSelectionModel().isEmpty());
        this.deleteMenu.setDisable(this.pokemonTable.getSelectionModel().isEmpty());
    }

    public void onPokemonEdited(DungeonEncounterTableItem item) {
        this.pokemonTable.refresh();
    }

    public void setupFor(Dungeon dungeon) {
        this.pokemonTable.getItems().clear();
        ArrayList<DungeonEncounter> w = dungeon.encountersData();
        for (DungeonEncounter e : w)
            this.pokemonTable.getItems().add(new DungeonEncounterTableItem(e.getModel()));
        this.onEdit();
    }

}

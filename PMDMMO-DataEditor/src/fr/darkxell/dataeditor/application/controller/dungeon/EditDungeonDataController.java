package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.darkxell.common.Registries;
import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.Dungeon.DungeonDirection;
import com.darkxell.common.dungeon.data.DungeonEncounter;
import com.darkxell.common.dungeon.data.DungeonItemGroup;
import com.darkxell.common.dungeon.data.DungeonRegistry;
import com.darkxell.common.dungeon.data.DungeonTrapGroup;
import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.dungeon.data.FloorSet;

import fr.darkxell.dataeditor.application.util.DungeonCreationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class EditDungeonDataController implements Initializable {

    public static EditDungeonDataController instance;

    @FXML
    public ComboBox<DungeonDirection> directionCombobox;
    @FXML
    public TextField floorsTextfield;
    @FXML
    public TextField idTextfield;
    @FXML
    public TextField limitTextfield;
    @FXML
    public CheckBox linkedCheckbox;
    @FXML
    public ComboBox<Dungeon> linkedCombobox;
    @FXML
    public TextField mapxTextfield;
    @FXML
    public TextField mapyTextfield;
    @FXML
    public CheckBox recruitsCheckbox;
    @FXML
    public TextField stickyTextfield;

    public int currentFloorCount() {
        int count = DungeonsTabController.instance.currentDungeon.floorCount;
        try {
            count = Integer.parseInt(this.floorsTextfield.getText());
        } catch (Exception e) {
        }
        return count;
    }

    public Dungeon generateDungeon(ArrayList<DungeonEncounter> pokemon, ArrayList<DungeonItemGroup> items,
            ArrayList<DungeonItemGroup> shopItems, ArrayList<DungeonItemGroup> buriedItems,
            ArrayList<DungeonTrapGroup> traps, ArrayList<FloorData> floorData, HashMap<Integer, FloorSet> weather)
            throws DungeonCreationException {
        int id, floorCount, timeLimit, stickyChance, linkedTo = -1, mapx, mapy;
        try {
            id = Integer.parseInt(this.idTextfield.getText());
        } catch (NumberFormatException e) {
            throw new DungeonCreationException("Invalid Dungeon ID: " + this.idTextfield.getText());
        }
        try {
            floorCount = Integer.parseInt(this.floorsTextfield.getText());
        } catch (NumberFormatException e) {
            throw new DungeonCreationException("Invalid Floor count: " + this.floorsTextfield.getText());
        }
        try {
            mapx = Integer.parseInt(this.mapxTextfield.getText());
        } catch (NumberFormatException e) {
            throw new DungeonCreationException("Invalid Map X: " + this.mapxTextfield.getText());
        }
        try {
            mapy = Integer.parseInt(this.mapyTextfield.getText());
        } catch (NumberFormatException e) {
            throw new DungeonCreationException("Invalid Map Y: " + this.mapyTextfield.getText());
        }
        try {
            timeLimit = Integer.parseInt(this.limitTextfield.getText());
        } catch (NumberFormatException e) {
            throw new DungeonCreationException("Invalid Time limit: " + this.limitTextfield.getText());
        }
        try {
            stickyChance = Integer.parseInt(this.stickyTextfield.getText());
        } catch (NumberFormatException e) {
            throw new DungeonCreationException("Invalid Sticky chance: " + this.stickyTextfield.getText());
        }

        if (this.linkedCheckbox.isSelected()) {
            Dungeon linked = this.linkedCombobox.getValue();
            if (linked == null)
                throw new DungeonCreationException("No Linked Dungeon was chosen");
            else
                linkedTo = linked.id;
        }

        return new Dungeon(id, floorCount, this.directionCombobox.getValue(), this.recruitsCheckbox.isSelected(),
                timeLimit, stickyChance, linkedTo, pokemon, items, shopItems, buriedItems, traps, floorData, weather,
                mapx, mapy);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        this.linkedCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> this.onLinkedChecked());
        this.directionCombobox.getItems().addAll(DungeonDirection.values());
        this.onLinkedChecked();
    }

    public void onLinkedChecked() {
        this.linkedCombobox.setDisable(!this.linkedCheckbox.isSelected());
    }

    public void setupFor(Dungeon dungeon) {
        this.idTextfield.setText(String.valueOf(dungeon.id));
        this.floorsTextfield.setText(String.valueOf(dungeon.floorCount));
        this.mapxTextfield.setText(String.valueOf(dungeon.mapx));
        this.mapyTextfield.setText(String.valueOf(dungeon.mapy));
        this.limitTextfield.setText(String.valueOf(dungeon.timeLimit));
        this.stickyTextfield.setText(String.valueOf(dungeon.stickyChance));
        this.recruitsCheckbox.setSelected(dungeon.recruitsAllowed);
        this.directionCombobox.setValue(dungeon.direction);

        DungeonRegistry dungeons = Registries.dungeons();

        this.linkedCheckbox.setSelected(dungeon.linkedTo != -1);
        this.linkedCombobox.getItems().clear();
        this.linkedCombobox.getItems().addAll(dungeons.toList());
        this.linkedCombobox.getItems().remove(DungeonsTabController.instance.currentDungeon);
        if (this.linkedCheckbox.isSelected())
            this.linkedCombobox.setValue(dungeons.find(dungeon.linkedTo));
    }

}

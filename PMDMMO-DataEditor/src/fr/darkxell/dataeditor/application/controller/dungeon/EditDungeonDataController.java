package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.Dungeon.DungeonDirection;
import com.darkxell.common.dungeon.data.DungeonRegistry;
import com.darkxell.common.model.dungeon.DungeonEncounterModel;
import com.darkxell.common.model.dungeon.DungeonItemGroupModel;
import com.darkxell.common.model.dungeon.DungeonModel;
import com.darkxell.common.model.dungeon.DungeonTrapGroupModel;
import com.darkxell.common.model.dungeon.DungeonWeatherModel;
import com.darkxell.common.model.dungeon.FloorDataModel;
import com.darkxell.common.registry.Registries;

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
        int count = DungeonsTabController.instance.currentDungeon.getFloorCount();
        try {
            count = Integer.parseInt(this.floorsTextfield.getText());
        } catch (Exception ignored) {
        }
        return count;
    }

    public Dungeon generateDungeon(ArrayList<DungeonEncounterModel> pokemon, ArrayList<DungeonItemGroupModel> items,
            ArrayList<DungeonItemGroupModel> shopItems, ArrayList<DungeonItemGroupModel> buriedItems,
            ArrayList<DungeonTrapGroupModel> traps, ArrayList<FloorDataModel> floorData,
            ArrayList<DungeonWeatherModel> weather) throws DungeonCreationException {
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
                linkedTo = linked.getID();
        }

        return new Dungeon(new DungeonModel(id, floorCount, this.directionCombobox.getValue(),
                this.recruitsCheckbox.isSelected(), timeLimit, stickyChance, linkedTo, pokemon, items, shopItems,
                buriedItems, traps, floorData, weather, mapx, mapy));
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
        this.idTextfield.setText(String.valueOf(dungeon.getID()));
        this.floorsTextfield.setText(String.valueOf(dungeon.getFloorCount()));
        this.mapxTextfield.setText(String.valueOf(dungeon.getMapX()));
        this.mapyTextfield.setText(String.valueOf(dungeon.getMapY()));
        this.limitTextfield.setText(String.valueOf(dungeon.getTimeLimit()));
        this.stickyTextfield.setText(String.valueOf(dungeon.getStickyChance()));
        this.recruitsCheckbox.setSelected(dungeon.isRecruitsAllowed());
        this.directionCombobox.setValue(dungeon.getDirection());

        DungeonRegistry dungeons = Registries.dungeons();

        this.linkedCheckbox.setSelected(dungeon.getLinkedTo() != -1);
        this.linkedCombobox.getItems().clear();
        this.linkedCombobox.getItems().addAll(dungeons.toList());
        this.linkedCombobox.getItems().remove(DungeonsTabController.instance.currentDungeon);
        if (this.linkedCheckbox.isSelected())
            this.linkedCombobox.setValue(dungeons.find(dungeon.getLinkedTo()));
    }

}

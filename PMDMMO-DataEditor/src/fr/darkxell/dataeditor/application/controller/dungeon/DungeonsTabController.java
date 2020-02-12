package fr.darkxell.dataeditor.application.controller.dungeon;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.Dungeon.DungeonDirection;
import com.darkxell.common.dungeon.data.DungeonRegistry;
import com.darkxell.common.registry.Registries;

import fr.darkxell.dataeditor.application.controls.CustomList;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;

public class DungeonsTabController implements Initializable, ListCellParent<Dungeon> {

    public static DungeonsTabController instance;

    /** Currently edited Dungeon. */
    public Dungeon currentDungeon;
    @FXML
    private ListView<Dungeon> dungeonsList;
    @FXML
    public EditDungeonController editDungeonController;
    @FXML
    private TitledPane editDungeonPane;

    Dungeon defaultDungeon(int id) {
        return new Dungeon(id, 1, DungeonDirection.UP, true, 2000, 0, -1, new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>(), 0, 0);
    }

    @Override
    public Node graphicFor(Dungeon item) {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        CustomList.setup(this, this.dungeonsList, "Dungeon", true, false, true, true, false);
        this.dungeonsList.getItems().addAll(Registries.dungeons().toList());
        this.dungeonsList.getItems().sort(Comparator.naturalOrder());

        this.editDungeonPane.setVisible(false);
    }

    @Override
    public void onCreate(Dungeon dungeon) {
        this.onCreateDungeon();
    }

    public void onCreateDungeon() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New Dungeon");
        dialog.setHeaderText(null);
        dialog.setContentText("Type in the ID of the new Dungeon.");
        Optional<String> name = dialog.showAndWait();
        if (name.isPresent())
            if (name.get().matches("\\d+")) {
                Dungeon d = this.defaultDungeon(Integer.parseInt(name.get()));
                DungeonRegistry dungeons = Registries.dungeons();
                if (dungeons.find(d.getID()) != null)
                    new Alert(AlertType.ERROR, "There is already a Dungeon with ID " + d.getID(), ButtonType.OK)
                            .showAndWait();
                else {
                    dungeons.register(d);
                    this.dungeonsList.getItems().add(d);
                    this.dungeonsList.getItems().sort(Comparator.naturalOrder());
                }
            } else
                new Alert(AlertType.ERROR, "Wrong ID: " + name.get(), ButtonType.OK);
    }

    @Override
    public void onDelete(Dungeon item) {
        if (item == this.currentDungeon) {
            this.currentDungeon = null;
            this.editDungeonPane.setVisible(false);
        }
        this.dungeonsList.getItems().remove(item);
        Registries.dungeons().unregister(item.getID());
    }

    @Override
    public void onEdit(Dungeon dungeon) {
        this.currentDungeon = dungeon;
        this.editDungeonPane.setVisible(true);
        this.editDungeonPane.setText(this.currentDungeon.name().toString());
        this.editDungeonController.setupFor(this.currentDungeon);
    }

    public void onEdited(Dungeon dungeon) {
        DungeonRegistry dungeons = Registries.dungeons();
        boolean idChanged = this.currentDungeon.getID() != dungeon.getID();
        if (idChanged && dungeons.find(dungeon.getID()) != null)
            new Alert(AlertType.ERROR, "Cannot save: There is already another Dungeon with ID " + dungeon.getID(),
                    ButtonType.OK).showAndWait();
        else {
            dungeons.unregister(this.currentDungeon.getID());
            dungeons.register(dungeon);
            this.dungeonsList.getItems().remove(this.currentDungeon);
            this.dungeonsList.getItems().add(dungeon);
            this.dungeonsList.getItems().sort(Comparator.naturalOrder());
            this.dungeonsList.getSelectionModel().select(dungeon);
            this.onEdit(dungeon);
        }
    }

    @Override
    public void onMove(Dungeon item, int newIndex) {
    }

    @Override
    public void onRename(Dungeon item, String name) {
    }

    public void onSaveAllDungeons() {
        Registries.dungeons().save(new File("../PMDMMO-common/resources/data/dungeons.xml"));
    }

}

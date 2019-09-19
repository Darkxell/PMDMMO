package fr.darkxell.dataeditor.application.controller.sprites;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;

import com.darkxell.client.resources.image.pokemon.body.PSDSequence;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpritesetData;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpritesets;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.XMLUtils;

import fr.darkxell.dataeditor.application.controller.animation.TestAnimationController;
import fr.darkxell.dataeditor.application.controls.CustomList;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.data.PokemonSpritesetManager;
import fr.darkxell.dataeditor.application.util.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

public class SpritesTabController implements Initializable, ListCellParent<PokemonSpritesetData> {
    public PokemonSpritesetData currentSprite;

    @FXML
    private VBox databox1;
    @FXML
    private VBox databox2;
    @FXML
    public EditGeneralController generalDataController;
    @FXML
    public EditSequencesController sequencesController;
    @FXML
    public EditTableController sequenceTableController;
    @FXML
    public ListView<PokemonSpritesetData> spritesList;
    @FXML
    public TestAnimationController testSpriteController;

    private PokemonSpritesetData generateData() {
        HashMap<Pair<PokemonSpriteState, Direction>, Integer> states = this.sequenceTableController.generateTable();
        HashSet<Integer> existing = new HashSet<>(states.values());
        HashMap<Integer, PSDSequence> sequences = this.sequencesController.generateSequences(existing);

        return new PokemonSpritesetData(this.currentSprite.id,
                this.generalDataController.bigShadowCheckbox.isSelected(),
                this.generalDataController.spriteset.spriteWidth, this.generalDataController.spriteset.spriteHeight,
                states, sequences);
    }

    @Override
    public Node graphicFor(PokemonSpritesetData item) {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.generalDataController.parent = this;
        this.sequenceTableController.parent = this;
        this.sequencesController.parent = this;

        this.databox1.setVisible(false);
        this.databox2.setVisible(false);
        this.testSpriteController.pokemonCombobox.setDisable(true);

        this.reloadList();
        CustomList.setup(this, this.spritesList, "Pokemon Sprite", true, false, true, true, false);
    }

    public void onCreate() {
        this.onCreate(null);
    }

    @Override
    public void onCreate(PokemonSpritesetData nullItem) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New Spriteset");
        dialog.setHeaderText(null);
        dialog.setContentText("Type in the ID of the Pokemon corresponding to this Spriteset.");
        Optional<String> name = dialog.showAndWait();
        if (name.isPresent())
            try {
                Integer id = Integer.parseInt(name.get());
                if (PokemonSpritesets.dataExists(id))
                    new Alert(AlertType.ERROR, "There is already a Spriteset with ID " + name.get(), ButtonType.OK)
                            .showAndWait();
                else {
                    PokemonSpritesetData data = new PokemonSpritesetData(id);
                    XMLUtils.saveFile(
                            FileManager
                                    .create(FileManager.filePaths.get(FileManager.POKEMON_SPRITES) + "/" + id + ".xml"),
                            data.toXML());
                    PokemonSpritesets.loadSpritesetData(data.id);
                    this.reloadList();
                    data = PokemonSpritesets.getData(data.id);
                    this.spritesList.getSelectionModel().select(data);
                    this.onEdit(data);
                }
            } catch (NumberFormatException e) {
                new Alert(AlertType.ERROR, "Wrong ID: " + name.get(), ButtonType.OK).showAndWait();
            }
    }

    @Override
    public void onDelete(PokemonSpritesetData item) {
        if (item == this.currentSprite) {
            this.currentSprite = null;
            this.databox1.setVisible(false);
            this.databox2.setVisible(false);
        }
        PokemonSpritesetManager.remove(item);
        this.reloadList();
    }

    public void onDimensionsChanged() {
        this.sequencesController.onDimensionsChanged();
    }

    @Override
    public void onEdit(PokemonSpritesetData item) {
        this.currentSprite = item;
        this.databox1.setVisible(true);
        this.databox2.setVisible(true);

        this.generalDataController.setupFor(item);
        this.sequenceTableController.setupFor(item);
        this.sequencesController.setupFor(item);
        PokemonSpecies s = Registries.species().find(item.id);
        if (s != null) {
            this.testSpriteController.pokemonCombobox.getSelectionModel().select(0);
            this.testSpriteController.pokemonCombobox.getSelectionModel().select(1);
            this.testSpriteController.pokemonCombobox.getSelectionModel().select(s);
        }
    }

    public void onExistingSequencesChanged(HashSet<Integer> existing) {
        this.sequencesController.onExistingSequencesChanged(existing);
    }

    @Override
    public void onMove(PokemonSpritesetData item, int newIndex) {
    }

    @Override
    public void onRename(PokemonSpritesetData item, String name) {
    }

    public void onSaveChanges() {
        PokemonSpritesetData data = this.generateData();
        if (data == null)
            return;
        XMLUtils.saveFile(
                FileManager.create(FileManager.filePaths.get(FileManager.POKEMON_SPRITES) + "/" + data.id + ".xml"),
                data.toXML());
        PokemonSpritesets.setSpritesetData(data.id, data);
        this.reloadList();
        this.spritesList.getSelectionModel().select(data);
        this.onEdit(data);
    }

    private void reloadList() {
        int selected = this.spritesList.getSelectionModel().getSelectedIndex();
        this.spritesList.getItems().clear();
        this.spritesList.getItems().addAll(PokemonSpritesets.listSpritesetData());
        if (selected != -1)
            this.spritesList.getSelectionModel().select(selected);
    }

}

package fr.darkxell.dataeditor.application.controller.cutscene;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;
import com.darkxell.common.Registries;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.Direction;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class EditEntityController implements Initializable {

    @FXML
    private CheckBox animatedCheckbox;
    @FXML
    public Button cancelButton;
    @FXML
    private ComboBox<String> entityTypeCombobox;
    @FXML
    private ComboBox<Direction> facingCombobox;
    @FXML
    private TextField idTextfield;
    @FXML
    private TextField memberTextfield;
    @FXML
    private ComboBox<String> modeCombobox;
    @FXML
    public Button okButton;
    @FXML
    private ComboBox<PokemonSpecies> speciesCombobox;
    @FXML
    private ComboBox<PokemonSpriteState> stateCombobox;
    @FXML
    private TextField xposTextfield;
    @FXML
    private TextField yposTextfield;

    public CutsceneEntity getEntity() {
        CutsceneEntity e;
        if (this.entityTypeCombobox.getSelectionModel().getSelectedIndex() == 0)
            e = new CutsceneEntity(Double.valueOf(this.idTextfield.getText()).intValue(),
                    Double.valueOf(this.xposTextfield.getText()), Double.valueOf(this.yposTextfield.getText()));
        else {
            Pokemon p;
            if (this.modeCombobox.getSelectionModel().getSelectedIndex() == 1)
                p = Persistence.player.getMember(Double.valueOf(this.memberTextfield.getText()).intValue());
            else
                p = this.speciesCombobox.getSelectionModel().getSelectedItem().generate(new Random(), 1);
            e = new CutscenePokemon(Double.valueOf(this.idTextfield.getText()).intValue(),
                    Double.valueOf(this.xposTextfield.getText()), Double.valueOf(this.yposTextfield.getText()), p,
                    this.stateCombobox.getSelectionModel().getSelectedItem(),
                    this.facingCombobox.getSelectionModel().getSelectedItem(), this.animatedCheckbox.isSelected());
        }
        return e;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.entityTypeCombobox.getItems().addAll("Default", "Pokemon");
        this.entityTypeCombobox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            modeCombobox.setDisable(newValue.equals(0));
            speciesCombobox.setDisable(newValue.equals(0));
            memberTextfield.setDisable(newValue.equals(0));
            stateCombobox.setDisable(newValue.equals(0));
            facingCombobox.setDisable(newValue.equals(0));
            animatedCheckbox.setDisable(newValue.equals(0));
        });
        this.entityTypeCombobox.getSelectionModel().select(0);

        this.modeCombobox.getItems().addAll("Pokemon species", "Team member");
        this.modeCombobox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            speciesCombobox.setVisible(newValue.equals(0));
            memberTextfield.setVisible(newValue.equals(1));
        });
        this.modeCombobox.getSelectionModel().select(0);

        this.speciesCombobox.getItems().addAll(Registries.species().toList());
        this.speciesCombobox.getSelectionModel().select(1);

        this.stateCombobox.getItems().addAll(PokemonSpriteState.values());
        this.stateCombobox.getSelectionModel().select(0);

        this.facingCombobox.getItems().addAll(Direction.DIRECTIONS);
        this.facingCombobox.getSelectionModel().select(0);

        Pattern pattern = Pattern.compile("-?\\d*(\\.\\d*)?");
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.xposTextfield.setTextFormatter(formatter);
        formatter = new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.yposTextfield.setTextFormatter(formatter);
        formatter = new TextFormatter<>(change -> {
            return Pattern.compile("-?\\d*").matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.idTextfield.setTextFormatter(formatter);
        formatter = new TextFormatter<>(change -> {
            return Pattern.compile("([0123])?").matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.memberTextfield.setTextFormatter(formatter);
    }

    public void onCancel() {
        CutsceneCreationController.editEntityPopup.close();
    }

    public void onOk() {
        this.onCancel();
        CutscenesTabController.instance.editCutsceneController.cutsceneCreationController
                .onEntityEdited(this.getEntity());
    }

    public void setupFor(CutsceneEntity entity) {
        this.idTextfield.setText(Integer.toString(entity.id));
        this.xposTextfield.setText(Double.toString(entity.xPos));
        this.yposTextfield.setText(Double.toString(entity.yPos));
        this.entityTypeCombobox.getSelectionModel().select(0);

        if (entity instanceof CutscenePokemon) {
            CutscenePokemon pokemon = (CutscenePokemon) entity;
            this.entityTypeCombobox.getSelectionModel().select(1);
            this.animatedCheckbox.setSelected(pokemon.animated);
            this.facingCombobox.getSelectionModel().select(pokemon.facing);
            this.stateCombobox.getSelectionModel().select(pokemon.currentState);
            if (Persistence.player.isAlly(pokemon.instanciated)) {
                this.modeCombobox.getSelectionModel().select(1);
                this.memberTextfield.setText(Integer.toString(Persistence.player.positionInTeam(pokemon.instanciated)));
            } else {
                this.modeCombobox.getSelectionModel().select(0);
                this.speciesCombobox.getSelectionModel().select(Registries.species().find(pokemon.pokemonid));
            }
        }
    }

}

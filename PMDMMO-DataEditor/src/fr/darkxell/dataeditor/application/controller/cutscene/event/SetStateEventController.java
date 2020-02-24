package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.client.model.cutscene.CutsceneEntityModel;
import com.darkxell.client.model.cutscene.CutscenePokemonModel;
import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.SetStateCutsceneEventModel;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class SetStateEventController extends EventController {

    @FXML
    private ComboBox<PokemonSpriteState> stateCombobox;
    @FXML
    private ComboBox<CutsceneEntityModel> targetCombobox;

    @Override
    public CutsceneEventModel generateEvent() {
        return new SetStateCutsceneEventModel(this.id(),
                this.targetCombobox.getSelectionModel().getSelectedItem().getCutsceneID(),
                this.stateCombobox.getSelectionModel().getSelectedItem());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.targetCombobox.getItems().addAll(EditCutsceneController.instance
                .listAvailableEntities(EditCutsceneController.instance.listManager.editing));
        this.targetCombobox.getItems().removeIf(e -> !(e instanceof CutscenePokemonModel));
        if (!this.targetCombobox.getItems().isEmpty())
            this.targetCombobox.getSelectionModel().select(0);

        this.stateCombobox.getItems().addAll(PokemonSpriteState.values());
        this.stateCombobox.getSelectionModel().select(0);
    }

    @Override
    public void setup(CutsceneEventModel event) {
        super.setup(event);
        for (CutsceneEntityModel e : this.targetCombobox.getItems())
            if (e.getCutsceneID() == ((SetStateCutsceneEventModel) event).getTarget()) {
                this.targetCombobox.getSelectionModel().select(e);
                break;
            }
        this.stateCombobox.getSelectionModel().select(((SetStateCutsceneEventModel) event).getState());
    }

}

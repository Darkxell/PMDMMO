package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.client.model.cutscene.CutsceneEntityModel;
import com.darkxell.client.model.cutscene.CutscenePokemonModel;
import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.SetAnimatedCutsceneEventModel;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

public class SetAnimatedEventController extends EventController {

    @FXML
    private CheckBox animatedCheckbox;
    @FXML
    private ComboBox<CutsceneEntityModel> targetCombobox;

    @Override
    public CutsceneEventModel generateEvent() {
        return new SetAnimatedCutsceneEventModel(this.id(),
                this.targetCombobox.getSelectionModel().getSelectedItem().getCutsceneID(),
                this.animatedCheckbox.isSelected());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.targetCombobox.getItems().addAll(EditCutsceneController.instance
                .listAvailableEntities(EditCutsceneController.instance.listManager.editing));
        this.targetCombobox.getItems().removeIf(e -> !(e instanceof CutscenePokemonModel));
        if (!this.targetCombobox.getItems().isEmpty())
            this.targetCombobox.getSelectionModel().select(0);
    }

    @Override
    public void setup(CutsceneEventModel event) {
        super.setup(event);
        for (CutsceneEntityModel e : this.targetCombobox.getItems())
            if (e.getCutsceneID() == ((SetAnimatedCutsceneEventModel) event).getTarget()) {
                this.targetCombobox.getSelectionModel().select(e);
                break;
            }
        this.animatedCheckbox.setSelected(((SetAnimatedCutsceneEventModel) event).isAnimated());
    }

}

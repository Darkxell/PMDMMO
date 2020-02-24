package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.SpawnCutsceneEventModel;

import fr.darkxell.dataeditor.application.controller.cutscene.EditEntityController;
import javafx.fxml.FXML;

public class SpawnEventController extends EventController {

    @FXML
    private EditEntityController editEntityController;

    @Override
    public CutsceneEventModel generateEvent() {
        return new SpawnCutsceneEventModel(this.id(), this.editEntityController.getEntity());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.editEntityController.okButton.setVisible(false);
        this.editEntityController.cancelButton.setVisible(false);
    }

    @Override
    public void setup(CutsceneEventModel event) {
        super.setup(event);
        this.editEntityController.setupFor(((SpawnCutsceneEventModel) event).getEntity());
    }

}

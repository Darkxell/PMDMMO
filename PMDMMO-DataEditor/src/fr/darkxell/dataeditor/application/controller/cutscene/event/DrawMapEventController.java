package fr.darkxell.dataeditor.application.controller.cutscene.event;

import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.DrawMapCutsceneEventModel;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

public class DrawMapEventController extends EventController {

    @FXML
    private CheckBox drawCheckbox;

    @Override
    public CutsceneEventModel generateEvent() {
        return new DrawMapCutsceneEventModel(this.id(), this.drawCheckbox.isSelected());
    }

    @Override
    public void setup(CutsceneEventModel event) {
        super.setup(event);
        this.drawCheckbox.setSelected(((DrawMapCutsceneEventModel) event).getDrawMap());
    }

}

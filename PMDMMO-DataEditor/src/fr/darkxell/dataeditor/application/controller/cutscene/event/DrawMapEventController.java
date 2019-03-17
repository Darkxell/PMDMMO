package fr.darkxell.dataeditor.application.controller.cutscene.event;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DrawMapCutsceneEvent;

public class DrawMapEventController extends EventController {

    @FXML
    private CheckBox drawCheckbox;

    @Override
    public CutsceneEvent generateEvent() {
        return new DrawMapCutsceneEvent(this.id(), this.drawCheckbox.isSelected());
    }

    @Override
    public void setup(CutsceneEvent event) {
        super.setup(event);
        this.drawCheckbox.setSelected(((DrawMapCutsceneEvent) event).shouldDraw);
    }

}

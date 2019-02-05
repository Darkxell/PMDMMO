package fr.darkxell.dataeditor.application.controller.cutscene.event;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.FunctionCutsceneEvent;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class FunctionEventController extends EventController {

    @FXML
    private TextField functionTextfield;

    @Override
    public CutsceneEvent generateEvent() {
        return new FunctionCutsceneEvent(this.id(), this.functionTextfield.getText());
    }

    @Override
    public void setup(CutsceneEvent event) {
        super.setup(event);
        if (((FunctionCutsceneEvent) event).functionID == null)
            this.functionTextfield.setText("");
        else
            this.functionTextfield.setText(((FunctionCutsceneEvent) event).functionID);
    }

}

package fr.darkxell.dataeditor.application.controller.cutscene.event;

import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.FunctionCutsceneEventModel;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class FunctionEventController extends EventController {

    @FXML
    private TextField functionTextfield;

    @Override
    public CutsceneEventModel generateEvent() {
        return new FunctionCutsceneEventModel(this.id(),
                this.functionTextfield.getText().equals("") ? null : this.functionTextfield.getText());
    }

    @Override
    public void setup(CutsceneEventModel event) {
        super.setup(event);
        if (((FunctionCutsceneEventModel) event).getFunctionID() == null)
            this.functionTextfield.setText("");
        else
            this.functionTextfield.setText(((FunctionCutsceneEventModel) event).getFunctionID());
    }

}

package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.DelayCutsceneEventModel;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class DelayEventController extends EventController {

    @FXML
    private TextField delayTextfield;

    @Override
    public CutsceneEventModel generateEvent() {
        return new DelayCutsceneEventModel(this.id(), Integer.parseInt(this.delayTextfield.getText()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        Pattern pattern = Pattern.compile("\\d*");
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.delayTextfield.setTextFormatter(formatter);
    }

    @Override
    public void setup(CutsceneEventModel event) {
        super.setup(event);
        this.delayTextfield.setText(String.valueOf(((DelayCutsceneEventModel) event).getTicks()));
    }

}

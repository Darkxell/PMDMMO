package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.CameraCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.MoveCutsceneEvent;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class CameraEventController extends EventController {

    @FXML
    private TextField speedTextfield;
    @FXML
    private TextField xposTextfield;
    @FXML
    private TextField yposTextfield;

    @Override
    public CutsceneEvent generateEvent() {
        double xpos = this.xposTextfield.getText().equals("") ? MoveCutsceneEvent.UNSPECIFIED
                : Double.valueOf(this.xposTextfield.getText());
        double ypos = this.yposTextfield.getText().equals("") ? MoveCutsceneEvent.UNSPECIFIED
                : Double.valueOf(this.yposTextfield.getText());
        return new CameraCutsceneEvent(this.id(), xpos, ypos, Double.valueOf(this.speedTextfield.getText()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        this.speedTextfield.setText("1");
        Pattern pattern = Pattern.compile("(-?\\d*(\\.\\d*)?)?");
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.xposTextfield.setTextFormatter(formatter);
        formatter = new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.yposTextfield.setTextFormatter(formatter);
        formatter = new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.speedTextfield.setTextFormatter(formatter);
    }

    @Override
    public void setup(CutsceneEvent event) {
        super.setup(event);
        CameraCutsceneEvent ev = (CameraCutsceneEvent) event;
        this.xposTextfield.setText(ev.xPos == MoveCutsceneEvent.UNSPECIFIED ? "" : String.valueOf(ev.xPos));
        this.yposTextfield.setText(ev.yPos == MoveCutsceneEvent.UNSPECIFIED ? "" : String.valueOf(ev.yPos));
        this.speedTextfield.setText(String.valueOf(ev.speed));
    }

}

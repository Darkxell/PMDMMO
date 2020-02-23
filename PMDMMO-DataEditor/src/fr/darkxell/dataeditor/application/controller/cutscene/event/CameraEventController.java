package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.darkxell.client.model.cutscene.event.CameraCutsceneEventModel;
import com.darkxell.client.model.cutscene.event.CutsceneEventModel;

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
    public CutsceneEventModel generateEvent() {
        double xpos = this.xposTextfield.getText().equals("") ? null : Double.valueOf(this.xposTextfield.getText());
        double ypos = this.yposTextfield.getText().equals("") ? null : Double.valueOf(this.yposTextfield.getText());
        return new CameraCutsceneEventModel(this.id(), xpos, ypos, Double.valueOf(this.speedTextfield.getText()));
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
    public void setup(CutsceneEventModel event) {
        super.setup(event);
        CameraCutsceneEventModel ev = (CameraCutsceneEventModel) event;
        this.xposTextfield.setText(ev.getX() == null ? "" : String.valueOf(ev.getX()));
        this.yposTextfield.setText(ev.getY() == null ? "" : String.valueOf(ev.getY()));
        this.speedTextfield.setText(String.valueOf(ev.getSpeed()));
    }

}

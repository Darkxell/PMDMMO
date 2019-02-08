package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.mechanics.cutscene.event.MoveCutsceneEvent;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class MoveEventController extends EventController {

    @FXML
    private TextField speedTextfield;
    @FXML
    private ComboBox<CutsceneEntity> targetCombobox;
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
        return new MoveCutsceneEvent(this.id(), this.targetCombobox.getSelectionModel().getSelectedItem(), xpos, ypos,
                Double.valueOf(this.speedTextfield.getText()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.targetCombobox.getItems().addAll(EditCutsceneController.instance
                .listAvailableEntities(EditCutsceneController.instance.listManager.editing));
        this.targetCombobox.getItems().removeIf(e -> !(e instanceof CutscenePokemon));
        if (!this.targetCombobox.getItems().isEmpty())
            this.targetCombobox.getSelectionModel().select(0);

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
        MoveCutsceneEvent ev = (MoveCutsceneEvent) event;
        for (CutsceneEntity e : this.targetCombobox.getItems())
            if (e.id == ev.target) {
                this.targetCombobox.getSelectionModel().select(e);
                break;
            }
        this.xposTextfield.setText(ev.xPos == MoveCutsceneEvent.UNSPECIFIED ? "" : String.valueOf(ev.xPos));
        this.yposTextfield.setText(ev.yPos == MoveCutsceneEvent.UNSPECIFIED ? "" : String.valueOf(ev.yPos));
        this.speedTextfield.setText(String.valueOf(ev.speed));
    }

}

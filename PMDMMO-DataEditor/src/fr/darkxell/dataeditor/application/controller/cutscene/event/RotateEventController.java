package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.cutscene.event.RotateCutsceneEvent;
import com.darkxell.client.model.cutscene.CutsceneEntityModel;
import com.darkxell.client.model.cutscene.CutscenePokemonModel;
import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.RotateCutsceneEventModel;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class RotateEventController extends EventController {

    @FXML
    private TextField distanceTextfield;
    @FXML
    private TextField speedTextfield;
    @FXML
    private ComboBox<CutsceneEntityModel> targetCombobox;

    @Override
    public CutsceneEventModel generateEvent() {
        return new RotateCutsceneEventModel(this.id(),
                this.targetCombobox.getSelectionModel().getSelectedItem().getCutsceneID(),
                Double.valueOf(this.distanceTextfield.getText()).intValue(),
                Double.valueOf(this.speedTextfield.getText()).intValue());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.targetCombobox.getItems().addAll(EditCutsceneController.instance
                .listAvailableEntities(EditCutsceneController.instance.listManager.editing));
        this.targetCombobox.getItems().removeIf(e -> !(e instanceof CutscenePokemonModel));
        if (!this.targetCombobox.getItems().isEmpty())
            this.targetCombobox.getSelectionModel().select(0);

        Pattern pattern = Pattern.compile("-?\\d*");
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.distanceTextfield.setTextFormatter(formatter);

        Pattern pattern2 = Pattern.compile("-?\\d*");
        TextFormatter<String> formatter2 = new TextFormatter<>(change -> {
            return pattern2.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.speedTextfield.setTextFormatter(formatter2);
        this.speedTextfield.setText(String.valueOf(RotateCutsceneEvent.DEFAULT_SPEED));
    }

    @Override
    public void setup(CutsceneEventModel event) {
        super.setup(event);
        for (CutsceneEntityModel e : this.targetCombobox.getItems())
            if (e.getCutsceneID() == ((RotateCutsceneEventModel) event).getTarget()) {
                this.targetCombobox.getSelectionModel().select(e);
                break;
            }
        this.speedTextfield.setText(String.valueOf(((RotateCutsceneEventModel) event).getSpeed()));
        this.distanceTextfield.setText(String.valueOf(((RotateCutsceneEventModel) event).getDistance()));
    }

}

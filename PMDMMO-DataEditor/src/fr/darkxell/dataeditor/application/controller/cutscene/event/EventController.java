package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;
import com.darkxell.client.model.cutscene.event.CutsceneEventModel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public abstract class EventController implements Initializable {

    public static interface EventEditionListener {
        public List<CutsceneEventModel> availableEvents();

        public EventList listManager();

        public void onEditCancel();

        public void onEditConfirm(CutsceneEventModel e);

        public void onEventTypeCancel();

        public void onEventTypeSelect(CutsceneEventType type);
    }

    @FXML
    protected TextField idTextfield;
    public EventEditionListener listener;

    public abstract CutsceneEventModel generateEvent();

    public int id() {
        return Integer.parseInt(this.idTextfield.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Pattern pattern = Pattern.compile("-?\\d*");
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.idTextfield.setTextFormatter(formatter);
        this.idTextfield.setText("-1");
    }

    public void onCancel() {
        this.listener.onEditCancel();
    }

    public void onOk() {
        this.onCancel();
        CutsceneEventModel e = this.generateEvent();
        this.listener.onEditConfirm(e);
    }

    public void setup(CutsceneEventModel event) {
        this.idTextfield.setText(String.valueOf(event.getID()));
    }

}

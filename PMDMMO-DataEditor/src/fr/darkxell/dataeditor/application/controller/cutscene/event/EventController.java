package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent.CutsceneEventType;

public abstract class EventController implements Initializable {

    public static interface EventEditionListener {
        public List<CutsceneEvent> availableEvents();

        public EventList listManager();

        public void onEditCancel();

        public void onEditConfirm(CutsceneEvent e);

        public void onEventTypeCancel();

        public void onEventTypeSelect(CutsceneEventType type);
    }

    @FXML
    protected TextField idTextfield;
    public EventEditionListener listener;

    public abstract CutsceneEvent generateEvent();

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
        CutsceneEvent e = this.generateEvent();
        this.listener.onEditConfirm(e);
    }

    public void setup(CutsceneEvent event) {
        this.idTextfield.setText(String.valueOf(event.id));
    }

}

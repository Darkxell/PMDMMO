package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;
import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.OptionResultCutsceneEventModel;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import fr.darkxell.dataeditor.application.controller.cutscene.event.EventController.EventEditionListener;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class OptionResultEventController extends EventController implements EventEditionListener {

    @FXML
    private ListView<CutsceneEventModel> eventList;
    private EventList listManager;
    @FXML
    private TextField optionTextfield;
    @FXML
    private ComboBox<CutsceneEventModel> targetCombobox;

    @Override
    public List<CutsceneEventModel> availableEvents() {
        ArrayList<CutsceneEventModel> events = new ArrayList<>(this.listener.availableEvents());
        int i = events.indexOf(this.listener.listManager().editing);
        if (i == -1)
            i = 0;
        for (CutsceneEventModel e : this.eventList.getItems())
            events.add(i++, e);
        return events;
    }

    @Override
    public CutsceneEventModel generateEvent() {
        if (this.optionTextfield.getText().equals("")) {
            FXUtils.showAlert("Type in option lazyguy");
            return null;
        }
        if (this.targetCombobox.getValue().getID() == -1)
            FXUtils.showAlert(
                    "Warning: your target option selection event doesn't have and ID. Don't forget to add one!");
        return new OptionResultCutsceneEventModel(this.id(), Integer.parseInt(this.optionTextfield.getText()),
                this.targetCombobox.getValue().getID(), new ArrayList<>(this.eventList.getItems()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        (this.listManager = new EventList()).setup(this, this.eventList);

        Pattern pattern = Pattern.compile("\\d*");
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        this.optionTextfield.setTextFormatter(formatter);

        ArrayList<CutsceneEventModel> events = new ArrayList<>(EditCutsceneController.instance.eventList.getItems());
        events.removeIf(e -> e.type != CutsceneEventType.option);
        this.targetCombobox.getItems().addAll(events);
    }

    @Override
    public EventList listManager() {
        return this.listManager;
    }

    @Override
    public void onEditCancel() {
        this.listManager.editEventPopup.close();
    }

    @Override
    public void onEditConfirm(CutsceneEventModel event) {
        if (event == null)
            return;
        ObservableList<CutsceneEventModel> events = this.eventList.getItems();
        if (this.listManager.editing == null)
            events.add(event);
        else {
            int index = events.indexOf(this.listManager.editing);
            events.remove(index);
            events.add(index, event);
        }
    }

    @Override
    public void onEventTypeCancel() {
        this.listManager.selectEventTypePopup.close();
    }

    @Override
    public void onEventTypeSelect(CutsceneEventType type) {
        this.listManager.selectEventTypePopup.close();
        this.listManager.onCreate(null, type);
    }

    @Override
    public void setup(CutsceneEventModel event) {
        super.setup(event);

        int dialog = ((OptionResultCutsceneEventModel) event).getDialog();
        for (CutsceneEventModel m : this.targetCombobox.getItems())
            if (m.getID().equals(dialog)) {
                this.targetCombobox.setValue(m);
                break;
            }
        this.optionTextfield.setText(String.valueOf(((OptionResultCutsceneEventModel) event).getOption()));
        this.eventList.getItems().clear();
        this.eventList.getItems().addAll(((OptionResultCutsceneEventModel) event).getResults());
    }

}

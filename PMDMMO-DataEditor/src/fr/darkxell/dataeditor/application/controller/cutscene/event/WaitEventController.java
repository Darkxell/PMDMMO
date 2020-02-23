package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.WaitCutsceneEventModel;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class WaitEventController extends EventController {

    public static class EventListCell extends ListCell<CutsceneEventModel> {
        protected void updateItem(CutsceneEventModel item, boolean empty) {
            super.updateItem(item, empty);
            this.setText(empty ? null : item.toString());
            this.setGraphic(empty ? null : EditCutsceneController.instance.listManager.graphicFor(item));
        }
    }

    @FXML
    private Button addButton;
    @FXML
    private ListView<CutsceneEventModel> addedEventsList;
    @FXML
    private CheckBox allCheckbox;
    private ArrayList<CutsceneEventModel> allEvents = new ArrayList<>();
    @FXML
    private ListView<CutsceneEventModel> existingEventsList;
    @FXML
    private Button removeButton;
    private Comparator<CutsceneEventModel> sorter = Comparator.comparingInt(o -> allEvents.indexOf(o));

    private void add(CutsceneEventModel e) {
        this.existingEventsList.getItems().remove(e);
        this.addedEventsList.getItems().add(e);
        this.sortLists();
    }

    private void add(Integer e) {
        CutsceneEventModel target = null;
        for (CutsceneEventModel m : this.existingEventsList.getItems())
            if (m.getID().equals(e)) {
                target = m;
                break;
            }
        if (target != null)
            this.add(target);
    }

    public void addSelected() {
        CutsceneEventModel e = this.existingEventsList.getSelectionModel().getSelectedItem();
        if (e != null)
            this.add(e);
    }

    @Override
    public CutsceneEventModel generateEvent() {
        ArrayList<Integer> events = new ArrayList<>();

        if (!this.allCheckbox.isSelected()) {
            this.addedEventsList.getItems().forEach(e -> events.add(e.getID()));

            ArrayList<CutsceneEventModel> noid = new ArrayList<>(addedEventsList.getItems());
            noid.removeIf(e -> e.getID() != -1);
            if (!noid.isEmpty()) {
                String message = "The following events need an ID to be waited for properly:";
                for (CutsceneEventModel e : noid)
                    message += "\n" + e;
                new Alert(AlertType.WARNING, message, ButtonType.OK).showAndWait();
            }
        }
        return new WaitCutsceneEventModel(this.id(), events);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.allCheckbox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            this.addedEventsList.setDisable(newValue);
            this.existingEventsList.setDisable(newValue);
            this.addButton.setDisable(newValue);
            this.removeButton.setDisable(newValue);
        });

        this.existingEventsList.setCellFactory(param -> {
            return new EventListCell();
        });
        this.addedEventsList.setCellFactory(param -> {
            return new EventListCell();
        });

        this.allCheckbox.setSelected(true);
    }

    public void removeSelected() {
        CutsceneEventModel e = this.addedEventsList.getSelectionModel().getSelectedItem();
        if (e != null) {
            this.addedEventsList.getItems().remove(e);
            this.existingEventsList.getItems().add(e);
            this.sortLists();
        }
    }

    @Override
    public void setup(CutsceneEventModel event) {
        super.setup(event);
        this.allEvents.clear();

        List<CutsceneEventModel> events = this.listener.availableEvents();
        this.allEvents.addAll(events);
        if (this.listener.listManager().editing != null)
            this.allEvents.removeIf(e -> events.indexOf(e) >= events.indexOf(this.listener.listManager().editing));
        this.existingEventsList.getItems().addAll(this.allEvents);

        this.allCheckbox.setSelected(((WaitCutsceneEventModel) event).getEvents().isEmpty());
        if (!this.allCheckbox.isSelected())
            for (Integer e : ((WaitCutsceneEventModel) event).getEvents())
                this.add(e);
    }

    private void sortLists() {
        this.addedEventsList.getItems().sort(this.sorter);
        this.existingEventsList.getItems().sort(this.sorter);
    }

}

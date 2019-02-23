package fr.darkxell.dataeditor.application.controller.cutscene;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent.CutsceneEventType;

import fr.darkxell.dataeditor.application.controller.cutscene.event.EventController.EventEditionListener;
import fr.darkxell.dataeditor.application.util.FXUtils;

public class SelectEventTypeController implements Initializable {

    @FXML
    public ListView<CutsceneEventType> eventList;
    public EventEditionListener listener;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.eventList.getItems().addAll(CutsceneEventType.values());
        this.eventList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            this.onSelect(newValue);
        });
        this.eventList.setCellFactory(param -> {
            return new ListCell<CutsceneEventType>() {
                @Override
                protected void updateItem(CutsceneEventType item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(item == null ? null : item.description);
                    if (item == null)
                        return;
                    Image fxImage = SwingFXUtils.toFXImage(FXUtils.getIcon("/icons/events/" + item.name() + ".png"),
                            null);
                    this.setGraphic(new ImageView(fxImage));
                }
            };
        });
    }

    public void onCancel() {
        this.listener.onEventTypeCancel();
    }

    private void onSelect(CutsceneEventType type) {
        this.listener.onEventTypeSelect(type);
    }

}

package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent.CutsceneEventType;
import com.darkxell.client.mechanics.cutscene.event.WaitCutsceneEvent;

import fr.darkxell.dataeditor.application.DataEditor;
import fr.darkxell.dataeditor.application.controller.cutscene.SelectEventTypeController;
import fr.darkxell.dataeditor.application.controller.cutscene.event.EventController.EventEditionListener;
import fr.darkxell.dataeditor.application.controls.CustomList;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.util.FXUtils;

public class EventList implements ListCellParent<CutsceneEvent> {
    public Stage editEventPopup;
    public CutsceneEvent editing;
    private ListView<CutsceneEvent> eventList;
    private EventEditionListener listener;
    public Stage selectEventTypePopup;

    @Override
    public Node graphicFor(CutsceneEvent item) {
        if (item == null)
            return null;
        Image fxImage = SwingFXUtils.toFXImage(FXUtils.getIcon("/icons/events/" + item.type.name() + ".png"), null);
        ImageView imageView = new ImageView(fxImage);
        return imageView;
    }

    @Override
    public void onCreate(CutsceneEvent event) {
        if (event != null)
            this.onCreate(event, event.type);
        else
            try {
                FXMLLoader loader = new FXMLLoader(
                        DataEditor.class.getResource("/layouts/cutscenes/select_event_type.fxml"));
                Parent root = loader.load();
                SelectEventTypeController controller = loader.getController();
                controller.listener = this.listener;
                this.selectEventTypePopup = FXUtils.showPopup(root, "New Event");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void onCreate(CutsceneEvent event, CutsceneEventType type) {
        this.editing = event;
        try {
            FXMLLoader loader = new FXMLLoader(
                    DataEditor.class.getResource("/layouts/cutscenes/events/" + type.name() + ".fxml"));
            Parent root = loader.load();
            EventController controller = loader.getController();
            controller.listener = this.listener;
            if (event != null)
                controller.setup(event);
            this.editEventPopup = FXUtils.showPopup(root, (event == null ? "New" : "Edit") + " Event");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDelete(CutsceneEvent item) {
        this.eventList.getItems().remove(item);
    }

    @Override
    public void onEdit(CutsceneEvent item) {
        this.onCreate(item);
    }

    @Override
    public void onMove(CutsceneEvent item, int newIndex) {
        if (item instanceof WaitCutsceneEvent)
            ((WaitCutsceneEvent) item).events
                    .removeIf(e -> this.eventList.getItems().indexOf(e) >= this.eventList.getItems().indexOf(item));
        ObservableList<CutsceneEvent> ev = this.eventList.getItems();
        ArrayList<CutsceneEvent> e = new ArrayList<>(ev);
        ev.clear(); // Refreshing display
        ev.addAll(e);
    }

    @Override
    public void onRename(CutsceneEvent item, String name) {
    }

    public void setup(EventEditionListener listener, ListView<CutsceneEvent> eventList) {
        this.eventList = eventList;
        this.listener = listener;
        CustomList.setup(this, eventList, "Cutscene Event", true, false, true, true, true);
    }

}

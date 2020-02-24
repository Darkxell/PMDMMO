package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.io.IOException;
import java.util.ArrayList;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;
import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.WaitCutsceneEventModel;

import fr.darkxell.dataeditor.application.DataEditor;
import fr.darkxell.dataeditor.application.controller.cutscene.SelectEventTypeController;
import fr.darkxell.dataeditor.application.controller.cutscene.event.EventController.EventEditionListener;
import fr.darkxell.dataeditor.application.controls.CustomList;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class EventList implements ListCellParent<CutsceneEventModel> {
    public Stage editEventPopup;
    public CutsceneEventModel editing;
    private ListView<CutsceneEventModel> eventList;
    private EventEditionListener listener;
    public Stage selectEventTypePopup;

    @Override
    public Node graphicFor(CutsceneEventModel item) {
        if (item == null)
            return null;
        Image fxImage = SwingFXUtils.toFXImage(FXUtils.getIcon("/icons/events/" + item.type.name() + ".png"), null);
        ImageView imageView = new ImageView(fxImage);
        return imageView;
    }

    @Override
    public void onCreate(CutsceneEventModel event) {
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

    public void onCreate(CutsceneEventModel event, CutsceneEventType type) {
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
    public void onDelete(CutsceneEventModel item) {
        this.eventList.getItems().remove(item);
    }

    @Override
    public void onEdit(CutsceneEventModel item) {
        this.onCreate(item);
    }

    @Override
    public void onMove(CutsceneEventModel item, int newIndex) {
        if (item instanceof WaitCutsceneEventModel) {
            ArrayList<Integer> toremove = new ArrayList<>();
            for (Integer i : ((WaitCutsceneEventModel) item).getEvents())
                for (int e = this.eventList.getItems().indexOf(item); e < this.eventList.getItems().size(); ++e)
                    if (this.eventList.getItems().get(e).getID().equals(i)) {
                        toremove.add(i);
                        break;
                    }
            ((WaitCutsceneEventModel) item).getEvents().removeAll(toremove);
        }
        ObservableList<CutsceneEventModel> ev = this.eventList.getItems();
        ArrayList<CutsceneEventModel> e = new ArrayList<>(ev);
        ev.clear(); // Refreshing display
        ev.addAll(e);
    }

    @Override
    public void onRename(CutsceneEventModel item, String name) {
    }

    public void setup(EventEditionListener listener, ListView<CutsceneEventModel> eventList) {
        this.eventList = eventList;
        this.listener = listener;
        CustomList.setup(this, eventList, "Cutscene Event", true, false, true, true, true);
    }

}

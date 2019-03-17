package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DialogCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DialogCutsceneEvent.CutsceneDialogScreen;

import fr.darkxell.dataeditor.application.DataEditor;
import fr.darkxell.dataeditor.application.controls.CustomList;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.util.FXUtils;

public class DialogEventController extends EventController implements ListCellParent<CutsceneDialogScreen> {

    public static Stage editDialogPopup;
    public static CutsceneDialogScreen editing;
    public static DialogEventController instance;

    @FXML
    private CheckBox narratorCheckbox;
    @FXML
    private ListView<CutsceneDialogScreen> screenList;

    @Override
    public CutsceneEvent generateEvent() {
        if (this.screenList.getItems().size() == 0)
            return null;
        return new DialogCutsceneEvent(this.id(), this.narratorCheckbox.isSelected(),
                new ArrayList<>(this.screenList.getItems()));
    }

    @Override
    public Node graphicFor(CutsceneDialogScreen item) {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        super.initialize(location, resources);
        CustomList.setup(this, this.screenList, "Dialog Screen", true, false, true, true, true);
    }

    public void onCreate() {
        this.onCreate(null);
    }

    @Override
    public void onCreate(CutsceneDialogScreen nullItem) {
        this.onEdit(nullItem);
    }

    @Override
    public void onDelete(CutsceneDialogScreen item) {
        this.screenList.getItems().remove(item);
    }

    @Override
    public void onEdit(CutsceneDialogScreen item) {
        try {
            editing = item;
            FXMLLoader loader = new FXMLLoader(
                    DataEditor.class.getResource("/layouts/cutscenes/events/edit_dialog.fxml"));
            editDialogPopup = FXUtils.showPopup(loader.load(), (item == null ? "New" : "Edit") + " Dialog");
            if (item != null)
                ((EditDialogController) loader.getController()).setup(item);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // this.screenList.getItems().add(new CutsceneDialogScreen(String.valueOf(Math.random()), false, -1, -1));
    }

    public void onEditConfirm(CutsceneDialogScreen screen) {
        ObservableList<CutsceneDialogScreen> events = this.screenList.getItems();
        if (editing == null)
            events.add(screen);
        else {
            int index = events.indexOf(editing);
            events.remove(index);
            events.add(index, screen);
        }
    }

    @Override
    public void onMove(CutsceneDialogScreen item, int newIndex) {
    }

    @Override
    public void onRename(CutsceneDialogScreen item, String name) {
    }

    @Override
    public void setup(CutsceneEvent event) {
        super.setup(event);
        this.narratorCheckbox.setSelected(((DialogCutsceneEvent) event).isNarratorDialog);
        this.screenList.getItems().addAll(((DialogCutsceneEvent) event).screens);
    }

}

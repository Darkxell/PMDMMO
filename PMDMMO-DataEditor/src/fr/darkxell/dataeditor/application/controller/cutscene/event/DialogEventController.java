package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.darkxell.client.model.cutscene.common.CutsceneDialogScreenModel;
import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.DialogCutsceneEventModel;

import fr.darkxell.dataeditor.application.DataEditor;
import fr.darkxell.dataeditor.application.controls.CustomList;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class DialogEventController extends EventController implements ListCellParent<CutsceneDialogScreenModel> {

    public static Stage editDialogPopup;
    public static CutsceneDialogScreenModel editing;
    public static DialogEventController instance;

    @FXML
    private CheckBox narratorCheckbox;
    @FXML
    private ListView<CutsceneDialogScreenModel> screenList;

    @Override
    public CutsceneEventModel generateEvent() {
        if (this.screenList.getItems().size() == 0)
            return null;
        return new DialogCutsceneEventModel(this.id(), this.narratorCheckbox.isSelected(),
                new ArrayList<>(this.screenList.getItems()));
    }

    @Override
    public Node graphicFor(CutsceneDialogScreenModel item) {
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
    public void onCreate(CutsceneDialogScreenModel nullItem) {
        this.onEdit(nullItem);
    }

    @Override
    public void onDelete(CutsceneDialogScreenModel item) {
        this.screenList.getItems().remove(item);
    }

    @Override
    public void onEdit(CutsceneDialogScreenModel item) {
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

    public void onEditConfirm(CutsceneDialogScreenModel screen) {
        ObservableList<CutsceneDialogScreenModel> events = this.screenList.getItems();
        if (editing == null)
            events.add(screen);
        else {
            int index = events.indexOf(editing);
            events.remove(index);
            events.add(index, screen);
        }
    }

    @Override
    public void onMove(CutsceneDialogScreenModel item, int newIndex) {
    }

    @Override
    public void onRename(CutsceneDialogScreenModel item, String name) {
    }

    @Override
    public void setup(CutsceneEventModel event) {
        super.setup(event);
        this.narratorCheckbox.setSelected(((DialogCutsceneEventModel) event).getIsNarratorDialog());
        this.screenList.getItems().addAll(((DialogCutsceneEventModel) event).getScreens());
    }

}

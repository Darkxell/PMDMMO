package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.MusicCutsceneEventModel;
import com.darkxell.client.resources.music.SoundsHolder;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class MusicEventController extends EventController {

    @FXML
    private ComboBox<String> musicCombobox;

    @Override
    public CutsceneEventModel generateEvent() {
        String id = this.musicCombobox.getSelectionModel().getSelectedItem();
        if (id == null || id.equals("No music"))
            id = "null";
        return new MusicCutsceneEventModel(this.id(), id);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.reloadCombobox();
    }

    public void onReloadMusics() {
        SoundsHolder.reloadSoundtracks();
        this.reloadCombobox();
    }

    private void reloadCombobox() {
        String s = this.musicCombobox.getSelectionModel().getSelectedItem();
        this.musicCombobox.getItems().clear();
        this.musicCombobox.getItems().add("No music");
        this.musicCombobox.getItems().addAll(SoundsHolder.soundtrackNames);
        if (this.musicCombobox.getItems().contains(s))
            this.musicCombobox.getSelectionModel().select(s);
        else
            this.musicCombobox.getSelectionModel().select(0);
    }

    @Override
    public void setup(CutsceneEventModel event) {
        super.setup(event);
        String id = ((MusicCutsceneEventModel) event).getSoundtrackID();
        if (id != null && id.equals("null"))
            id = "No music";
        this.musicCombobox.getSelectionModel().select(id);
    }

}

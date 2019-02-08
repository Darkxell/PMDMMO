package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.MusicCutsceneEvent;
import com.darkxell.client.resources.music.SoundsHolder;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class MusicEventController extends EventController {

    @FXML
    private ComboBox<String> musicCombobox;

    @Override
    public CutsceneEvent generateEvent() {
        return new MusicCutsceneEvent(this.id(), this.musicCombobox.getSelectionModel().getSelectedItem());
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
        this.musicCombobox.getItems().addAll(SoundsHolder.soundtrackNames);
        if (this.musicCombobox.getItems().contains(s))
            this.musicCombobox.getSelectionModel().select(s);
        else
            this.musicCombobox.getSelectionModel().select(0);
    }

    @Override
    public void setup(CutsceneEvent event) {
        super.setup(event);
        this.musicCombobox.getSelectionModel().select(((MusicCutsceneEvent) event).soundtrackID);
    }

}

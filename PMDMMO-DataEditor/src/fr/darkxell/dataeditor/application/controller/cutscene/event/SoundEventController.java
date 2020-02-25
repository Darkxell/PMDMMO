package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.SoundCutsceneEventModel;
import com.darkxell.client.resources.music.SoundsHolder;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

public class SoundEventController extends EventController {

    @FXML
    private CheckBox overMusicCheckbox;
    @FXML
    private ComboBox<String> soundCombobox;

    @Override
    public CutsceneEventModel generateEvent() {
        return new SoundCutsceneEventModel(this.id(), this.soundCombobox.getSelectionModel().getSelectedItem(),
                this.overMusicCheckbox.isSelected());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.reloadCombobox();
    }

    public void onReloadSounds() {
        SoundsHolder.reloadSfx();
        this.reloadCombobox();
    }

    private void reloadCombobox() {
        String s = this.soundCombobox.getSelectionModel().getSelectedItem();
        this.soundCombobox.getItems().clear();
        this.soundCombobox.getItems().addAll(SoundsHolder.sfxNames);
        if (this.soundCombobox.getItems().contains(s))
            this.soundCombobox.getSelectionModel().select(s);
        else
            this.soundCombobox.getSelectionModel().select(0);
    }

    @Override
    public void setup(CutsceneEventModel event) {
        super.setup(event);
        this.soundCombobox.getSelectionModel().select(((SoundCutsceneEventModel) event).getSoundID());
        this.overMusicCheckbox.setSelected(((SoundCutsceneEventModel) event).getPlayOverMusic());
    }

}

package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.Animations.AnimationGroup;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.mechanics.cutscene.event.AnimateCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.AnimateCutsceneEvent.AnimateCutsceneEventMode;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import fr.darkxell.dataeditor.application.util.AnimationListItem;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class AnimateEventController extends EventController {

    @FXML
    private ComboBox<AnimationListItem> animationCombobox;
    @FXML
    private RadioButton playRadio;
    @FXML
    private RadioButton startRadio;
    @FXML
    private RadioButton stopRadio;
    @FXML
    private ComboBox<CutsceneEntity> targetCombobox;

    @Override
    public CutsceneEvent generateEvent() {
        AnimateCutsceneEventMode mode = AnimateCutsceneEventMode.PLAY;
        if (this.playRadio.isSelected())
            mode = AnimateCutsceneEventMode.PLAY;
        else if (this.startRadio.isSelected())
            mode = AnimateCutsceneEventMode.START;
        else if (this.stopRadio.isSelected())
            mode = AnimateCutsceneEventMode.STOP;
        return new AnimateCutsceneEvent(this.id(), this.animationCombobox.getValue().id, mode,
                this.targetCombobox.getValue());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.targetCombobox.getItems().addAll(EditCutsceneController.instance
                .listAvailableEntities(EditCutsceneController.instance.listManager.editing));
        this.targetCombobox.getItems().removeIf(e -> !(e instanceof CutscenePokemon));
        if (!this.targetCombobox.getItems().isEmpty())
            this.targetCombobox.getSelectionModel().select(0);

        String[] anims = Animations.listAnimations();
        for (String anim : anims) {
            AnimationListItem item = AnimationListItem.create(anim);
            if (item != null && item.group == AnimationGroup.Custom)
                this.animationCombobox.getItems().add(item);
        }

        this.animationCombobox.getSelectionModel().select(0);
        this.animationCombobox.getItems().sort(Comparator.naturalOrder());

        ToggleGroup group = new ToggleGroup();
        this.playRadio.setToggleGroup(group);
        this.startRadio.setToggleGroup(group);
        this.stopRadio.setToggleGroup(group);
    }

    @Override
    public void setup(CutsceneEvent event) {
        super.setup(event);
        for (CutsceneEntity e : this.targetCombobox.getItems())
            if (e.id == ((AnimateCutsceneEvent) event).target) {
                this.targetCombobox.getSelectionModel().select(e);
                break;
            }

        for (AnimationListItem i : this.animationCombobox.getItems())
            if (i.id == ((AnimateCutsceneEvent) event).animationID)
                this.animationCombobox.setValue(i);

        AnimateCutsceneEventMode mode = ((AnimateCutsceneEvent) event).mode;
        if (mode == AnimateCutsceneEventMode.PLAY)
            this.playRadio.setSelected(true);
        else if (mode == AnimateCutsceneEventMode.START)
            this.startRadio.setSelected(true);
        else if (mode == AnimateCutsceneEventMode.STOP)
            this.stopRadio.setSelected(true);
    }

}

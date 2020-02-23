package fr.darkxell.dataeditor.application.controller.animation;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.darkxell.client.model.animation.AnimationModel;
import com.darkxell.client.model.animation.AnimationVariantModel;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.StringUtil;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class EditVariantController extends EditAnimationController {

    @FXML
    public CheckBox alsoplayCheckbox;
    @FXML
    public CheckBox alsoplayDelayCheckbox;
    @FXML
    public CheckBox animMovementCheckbox;
    @FXML
    public CheckBox backspritesCheckbox;
    @FXML
    public CheckBox clonesCheckbox;
    @FXML
    public CheckBox customAnimationCheckbox;
    public AnimationModel data;
    @FXML
    public CheckBox delayCheckbox;
    @FXML
    public CheckBox loopCheckbox;
    @FXML
    public RadioButton noStateRadio, hasStateRadio;
    @FXML
    public CheckBox orderCheckbox;
    @FXML
    public CheckBox overlayCheckbox;
    @FXML
    public CheckBox playsforeachtargetValueCheckbox;
    @FXML
    public CheckBox pokemonMovementCheckbox;
    @FXML
    public CheckBox soundCheckbox;
    @FXML
    public CheckBox soundDelayCheckbox;
    @FXML
    public CheckBox spriteDurationCheckbox;
    @FXML
    public CheckBox spritesCheckbox;
    @FXML
    public CheckBox stateDelayCheckbox;
    public Direction variant;
    @FXML
    public CheckBox widthCheckbox, heightCheckbox;
    @FXML
    public CheckBox xCheckbox, yCheckbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        ToggleGroup group = new ToggleGroup();
        this.hasStateRadio.setToggleGroup(group);
        this.noStateRadio.setToggleGroup(group);

        group.selectedToggleProperty().addListener((o, ov, nv) -> onStateChange());

        this.alsoplayTextfield.setDisable(true);
        this.alsoplayDelayTextfield.setDisable(true);
        this.animMovementCombobox.setDisable(true);
        this.backspritesCombobox.setDisable(true);
        this.clonesTextfield.setDisable(true);
        this.delayTextfield.setDisable(true);
        this.heightTextfield.setDisable(true);
        this.loopTextfield.setDisable(true);
        this.orderTextfield.setDisable(true);
        this.overlayTextfield.setDisable(true);
        this.playsforeachtargetValueCheckbox.setDisable(true);
        this.pokemonMovementCombobox.setDisable(true);
        this.soundDelayTextfield.setDisable(true);
        this.soundTextfield.setDisable(true);
        this.spriteDurationTextfield.setDisable(true);
        this.spritesTextfield.setDisable(true);
        this.customSpritesRadio.setDisable(true);
        this.defaultSpritesRadio.setDisable(true);
        this.noSpritesRadio.setDisable(true);
        this.stateDelayTextfield.setDisable(true);
        this.stateCombobox.setDisable(true);
        this.hasStateRadio.setDisable(true);
        this.noStateRadio.setDisable(true);
        this.widthTextfield.setDisable(true);
        this.xTextfield.setDisable(true);
        this.yTextfield.setDisable(true);
        this.customAnimationIDTextfield.setDisable(true);

        this.alsoplayDelayCheckbox.selectedProperty().addListener((o, ov, nv) -> onAlsoplayDelayOverride());
        this.alsoplayCheckbox.selectedProperty().addListener((o, ov, nv) -> onAlsoplayOverride());
        this.animMovementCheckbox.selectedProperty().addListener((o, ov, nv) -> onAnimMovementOverride());
        this.backspritesCheckbox.selectedProperty().addListener((o, ov, nv) -> onBackspritesOverride());
        this.clonesCheckbox.selectedProperty().addListener((o, ov, nv) -> onClonesOverride());
        this.delayCheckbox.selectedProperty().addListener((o, ov, nv) -> onDelayOverride());
        this.loopCheckbox.selectedProperty().addListener((o, ov, nv) -> onLoopOverride());
        this.stateCheckbox.selectedProperty().addListener((o, ov, nv) -> onStateOverride());
        this.orderCheckbox.selectedProperty().addListener((o, ov, nv) -> onOrderOverride());
        this.overlayCheckbox.selectedProperty().addListener((o, ov, nv) -> onOverlayOverride());
        this.playforeachtargetCheckbox.selectedProperty().addListener((o, ov, nv) -> onPlaysforeachOverride());
        this.pokemonMovementCheckbox.selectedProperty().addListener((o, ov, nv) -> onPokemonMovementOverride());
        this.soundCheckbox.selectedProperty().addListener((o, ov, nv) -> onSoundOverride());
        this.soundDelayCheckbox.selectedProperty().addListener((o, ov, nv) -> onSoundDelayOverride());
        this.spriteDurationCheckbox.selectedProperty().addListener((o, ov, nv) -> onSpriteDurationOverride());
        this.spritesCheckbox.selectedProperty().addListener((o, ov, nv) -> onSpritesOverride());
        this.stateDelayCheckbox.selectedProperty().addListener((o, ov, nv) -> onStateDelayOverride());
        this.widthCheckbox.selectedProperty().addListener((o, ov, nv) -> onWidthOverride());
        this.heightCheckbox.selectedProperty().addListener((o, ov, nv) -> onHeightOverride());
        this.xCheckbox.selectedProperty().addListener((o, ov, nv) -> onXOverride());
        this.yCheckbox.selectedProperty().addListener((o, ov, nv) -> onYOverride());
        this.customAnimationCheckbox.selectedProperty().addListener((o, ov, nv) -> onCustomAnimationOverride());
    }

    public void onAlsoplayDelayOverride() {
        this.alsoplayDelayTextfield.setDisable(!this.alsoplayDelayCheckbox.isSelected());
    }

    public void onAlsoplayOverride() {
        this.alsoplayTextfield.setDisable(!this.alsoplayCheckbox.isSelected());
    }

    public void onAnimMovementOverride() {
        this.animMovementCombobox.setDisable(!this.animMovementCheckbox.isSelected());
    }

    public void onBackspritesOverride() {
        this.backspritesCombobox.setDisable(!this.backspritesCheckbox.isSelected());
    }

    @Override
    public void onCancelChanges() {
        EditAnimationController.variantPopup.close();
    }

    public void onClonesOverride() {
        this.clonesTextfield.setDisable(!this.clonesCheckbox.isSelected());
    }

    public void onCustomAnimationOverride() {
        this.customAnimationIDTextfield.setDisable(!this.customAnimationCheckbox.isSelected());
    }

    public void onDelayOverride() {
        this.delayTextfield.setDisable(!this.delayCheckbox.isSelected());
    }

    public void onHeightOverride() {
        this.heightTextfield.setDisable(!this.heightCheckbox.isSelected());
    }

    public void onLoopOverride() {
        this.loopTextfield.setDisable(!this.loopCheckbox.isSelected());
    }

    public void onOrderOverride() {
        this.orderTextfield.setDisable(!this.orderCheckbox.isSelected());
    }

    public void onOverlayOverride() {
        this.overlayTextfield.setDisable(!this.overlayCheckbox.isSelected());
    }

    public void onPlaysforeachOverride() {
        this.playsforeachtargetValueCheckbox.setDisable(!this.playforeachtargetCheckbox.isSelected());
    }

    public void onPokemonMovementOverride() {
        this.pokemonMovementCombobox.setDisable(!this.pokemonMovementCheckbox.isSelected());
    }

    @Override
    public void onSave() {
        AnimationVariantModel variant = this.data.getVariant(this.variant);
        this.update(this.data, variant);
        EditAnimationController.variantPopup.close();
    }

    public void onSoundDelayOverride() {
        this.soundDelayTextfield.setDisable(!this.soundDelayCheckbox.isSelected());
    }

    public void onSoundOverride() {
        this.soundTextfield.setDisable(!this.soundCheckbox.isSelected());
    }

    public void onSpriteDurationOverride() {
        this.spriteDurationTextfield.setDisable(!this.spriteDurationCheckbox.isSelected());
    }

    @Override
    public void onSpritesChange() {
        this.spritesTextfield.setDisable(!this.customSpritesRadio.isSelected() || !this.spritesCheckbox.isSelected());
    }

    public void onSpritesOverride() {
        boolean sprites = !this.spritesCheckbox.isSelected();
        this.spritesTextfield.setDisable(sprites || !this.customSpritesRadio.isSelected());
        this.customSpritesRadio.setDisable(sprites);
        this.defaultSpritesRadio.setDisable(sprites);
        this.noSpritesRadio.setDisable(sprites);
    }

    @Override
    public void onStateChange() {
        this.stateCombobox.setDisable(this.noStateRadio.isSelected() || !this.stateCheckbox.isSelected());
    }

    public void onStateDelayOverride() {
        this.stateDelayTextfield.setDisable(!this.stateDelayCheckbox.isSelected());
    }

    public void onStateOverride() {
        this.noStateRadio.setDisable(!this.stateCheckbox.isSelected());
        this.hasStateRadio.setDisable(!this.stateCheckbox.isSelected());
        this.onStateChange();
    }

    public void onWidthOverride() {
        this.widthTextfield.setDisable(!this.widthCheckbox.isSelected());
    }

    public void onXOverride() {
        this.xTextfield.setDisable(!this.xCheckbox.isSelected());
    }

    public void onYOverride() {
        this.yTextfield.setDisable(!this.yCheckbox.isSelected());
    }

    public void setup(AnimationModel parent, AnimationVariantModel model) {
        this.setupFor(parent, model);
        AnimationVariantModel def = parent.getDefaultModel();
        this.alsoplayCheckbox.setSelected(!Arrays.equals(model.getAlsoPlay(), def.getAlsoPlay()));
        this.alsoplayDelayCheckbox.setSelected(!Arrays.equals(model.getAlsoPlayDelay(), def.getAlsoPlayDelay()));
        this.animMovementCheckbox
                .setSelected(!StringUtil.equals(model.getAnimationMovement(), def.getAnimationMovement()));
        this.backspritesCheckbox.setSelected(model.getBackSpriteUsage() != def.getBackSpriteUsage());
        this.delayCheckbox.setSelected(!model.getDelayTime().equals(def.getDelayTime()));
        this.loopCheckbox.setSelected(!model.getLoopsFrom().equals(def.getLoopsFrom()));
        this.orderCheckbox.setSelected(!Arrays.equals(model.getSpriteOrder(), def.getSpriteOrder()));
        this.overlayCheckbox.setSelected(!model.getOverlay().equals(def.getOverlay()));
        this.playforeachtargetCheckbox.setSelected(!model.isPlaysForEachTarget().equals(def.isPlaysForEachTarget()));
        this.playsforeachtargetValueCheckbox.setSelected(model.isPlaysForEachTarget());
        this.pokemonMovementCheckbox
                .setSelected(!StringUtil.equals(model.getPokemonMovement(), def.getPokemonMovement()));
        this.soundCheckbox.setSelected(!StringUtil.equals(model.getSound(), def.getSound()));
        this.soundDelayCheckbox.setSelected(!model.getSoundDelay().equals(def.getSoundDelay()));
        this.spriteDurationCheckbox.setSelected(!model.getSpriteDuration().equals(def.getSpriteDuration()));
        this.widthCheckbox.setSelected(!model.getWidth().equals(def.getWidth()));
        this.heightCheckbox.setSelected(!model.getHeight().equals(def.getHeight()));
        this.xCheckbox.setSelected(!model.getGravityX().equals(def.getGravityX()));
        this.yCheckbox.setSelected(!model.getGravityY().equals(def.getGravityY()));
        this.spritesCheckbox.setSelected(!StringUtil.equals(model.getSprites(), def.getSprites()));
        this.customAnimationCheckbox
                .setSelected(StringUtil.equals(model.getCustomAnimation(), def.getCustomAnimation()));

        this.stateCheckbox.setSelected(model.getPokemonState() != def.getPokemonState());
        this.noStateRadio.setSelected(model.getPokemonState() == null);
        this.hasStateRadio.setSelected(model.getPokemonState() != null);
    }

    @Override
    protected AnimationModel update(AnimationModel parent, AnimationVariantModel model) {
        super.update(parent, model);
        AnimationVariantModel def = parent.getDefaultModel();

        if (!this.alsoplayCheckbox.isSelected())
            model.setAlsoPlay(def.getAlsoPlay().clone());
        if (!this.alsoplayDelayCheckbox.isSelected())
            model.setAlsoPlayDelay(def.getAlsoPlayDelay().clone());
        if (!this.animMovementCheckbox.isSelected())
            model.setAnimationMovement(def.getAnimationMovement());
        if (!this.backspritesCheckbox.isSelected())
            model.setBackSpriteUsage(def.getBackSpriteUsage());
        if (!this.delayCheckbox.isSelected())
            model.setDelayTime(def.getDelayTime());
        if (!this.loopCheckbox.isSelected())
            model.setLoopsFrom(def.getLoopsFrom());
        if (!this.stateCheckbox.isSelected())
            model.setPokemonState(this.noStateRadio.isSelected() ? null : def.getPokemonState());
        if (!this.orderCheckbox.isSelected())
            model.setSpriteOrder(def.getSpriteOrder().clone());
        if (!this.overlayCheckbox.isSelected())
            model.setOverlay(def.getOverlay());
        if (!this.playforeachtargetCheckbox.isSelected())
            model.setPlaysForEachTarget(def.isPlaysForEachTarget());
        else
            model.setPlaysForEachTarget(this.playsforeachtargetValueCheckbox.isSelected());
        if (!this.pokemonMovementCheckbox.isSelected())
            model.setPokemonMovement(def.getPokemonMovement());
        if (!this.soundCheckbox.isSelected())
            model.setSound(def.getSound());
        if (!this.soundDelayCheckbox.isSelected())
            model.setSoundDelay(def.getSoundDelay());
        if (!this.spriteDurationCheckbox.isSelected())
            model.setSpriteDuration(def.getSpriteDuration());
        if (!this.spritesCheckbox.isSelected())
            model.setSprites(def.getSprites());
        if (!this.stateDelayCheckbox.isSelected())
            model.setPokemonStateDelay(def.getPokemonStateDelay());
        if (!this.widthCheckbox.isSelected())
            model.setWidth(def.getWidth());
        if (!this.heightCheckbox.isSelected())
            model.setHeight(def.getHeight());
        if (!this.xCheckbox.isSelected())
            model.setGravityX(def.getGravityX());
        if (!this.yCheckbox.isSelected())
            model.setGravityY(def.getGravityY());
        if (!this.customAnimationCheckbox.isSelected())
            model.setCustomAnimation(def.getCustomAnimation());

        return parent;
    }

}

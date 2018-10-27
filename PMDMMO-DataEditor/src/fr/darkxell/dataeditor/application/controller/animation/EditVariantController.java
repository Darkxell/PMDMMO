package fr.darkxell.dataeditor.application.controller.animation;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.animation.AnimationData;
import com.darkxell.common.util.Direction;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class EditVariantController extends EditAnimationController
{

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
	public AnimationData data;
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
	public void initialize(URL location, ResourceBundle resources)
	{
		super.initialize(location, resources);

		ToggleGroup group = new ToggleGroup();
		this.hasStateRadio.setToggleGroup(group);
		this.noStateRadio.setToggleGroup(group);

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
	}

	public void onAlsoplayDelayOverride()
	{
		this.alsoplayDelayTextfield.setDisable(!this.alsoplayDelayCheckbox.isSelected());
	}

	public void onAlsoplayOverride()
	{
		this.alsoplayTextfield.setDisable(!this.alsoplayCheckbox.isSelected());
	}

	public void onAnimMovementOverride()
	{
		this.animMovementCombobox.setDisable(!this.animMovementCheckbox.isSelected());
	}

	public void onBackspritesOverride()
	{
		this.backspritesCombobox.setDisable(!this.backspritesCheckbox.isSelected());
	}

	public void onClonesOverride()
	{
		this.clonesTextfield.setDisable(!this.clonesCheckbox.isSelected());
	}

	public void onDelayOverride()
	{
		this.delayTextfield.setDisable(!this.delayCheckbox.isSelected());
	}

	public void onHeightOverride()
	{
		this.heightTextfield.setDisable(!this.heightCheckbox.isSelected());
	}

	public void onLoopOverride()
	{
		this.loopTextfield.setDisable(!this.loopCheckbox.isSelected());
	}

	public void onOrderOverride()
	{
		this.orderTextfield.setDisable(!this.orderCheckbox.isSelected());
	}

	public void onOverlayOverride()
	{
		this.overlayTextfield.setDisable(!this.overlayCheckbox.isSelected());
	}

	public void onPlaysforeachOverride()
	{
		this.playsforeachtargetValueCheckbox.setDisable(!this.playforeachtargetCheckbox.isSelected());
	}

	public void onPokemonMovementOverride()
	{
		this.pokemonMovementCombobox.setDisable(!this.pokemonMovementCheckbox.isSelected());
	}

	public void onSoundDelayOverride()
	{
		this.soundDelayTextfield.setDisable(!this.soundDelayCheckbox.isSelected());
	}

	public void onSoundOverride()
	{
		this.soundTextfield.setDisable(!this.soundCheckbox.isSelected());
	}

	public void onSpriteDurationOverride()
	{
		this.spriteDurationTextfield.setDisable(!this.spriteDurationCheckbox.isSelected());
	}

	public void onSpritesOverride()
	{
		boolean sprites = !this.spritesCheckbox.isSelected();
		this.spritesTextfield.setDisable(sprites);
		this.customSpritesRadio.setDisable(sprites);
		this.defaultSpritesRadio.setDisable(sprites);
		this.noSpritesRadio.setDisable(sprites);
	}

	@Override
	public void onStateChange()
	{
		this.stateCombobox.setDisable(this.noStateRadio.isSelected());
	}

	public void onStateDelayOverride()
	{
		this.stateDelayTextfield.setDisable(!this.stateDelayCheckbox.isSelected());
	}

	public void onStateOverride()
	{
		this.stateCombobox.setDisable(!this.stateCheckbox.isSelected());
		this.noStateRadio.setDisable(!this.stateCheckbox.isSelected());
		this.hasStateRadio.setDisable(!this.stateCheckbox.isSelected());
	}

	public void onWidthOverride()
	{
		this.widthTextfield.setDisable(!this.widthCheckbox.isSelected());
	}

	public void onXOverride()
	{
		this.xTextfield.setDisable(!this.xCheckbox.isSelected());
	}

	public void onYOverride()
	{
		this.yTextfield.setDisable(!this.yCheckbox.isSelected());
	}

	public void setup(AnimationData variant)
	{
		this.setupFor(variant);
		this.alsoplayCheckbox.setSelected(!Arrays.equals(variant.alsoPlay, this.data.alsoPlay));
		this.alsoplayDelayCheckbox.setSelected(!Arrays.equals(variant.alsoPlayDelay, this.data.alsoPlayDelay));
		this.animMovementCheckbox.setSelected(
				!((variant.animationMovement == null && this.data.animationMovement == null) || variant.animationMovement.equals(this.data.animationMovement)));
		this.backspritesCheckbox.setSelected(variant.backSpriteUsage != this.data.backSpriteUsage);
		this.clonesCheckbox.setSelected(!((variant.clones == null && this.data.clones == null) || variant.clones.equals(this.data.clones)));
		this.delayCheckbox.setSelected(variant.delayTime != this.data.delayTime);
		this.loopCheckbox.setSelected(variant.loopsFrom != this.data.loopsFrom);
		this.orderCheckbox.setSelected(!Arrays.equals(variant.spriteOrder, this.data.spriteOrder));
		this.overlayCheckbox.setSelected(variant.overlay != this.data.overlay);
		this.playforeachtargetCheckbox.setSelected(variant.playsForEachTarget != this.data.playsForEachTarget);
		this.playsforeachtargetValueCheckbox.setSelected(variant.playsForEachTarget);
		this.pokemonMovementCheckbox.setSelected(
				!((variant.pokemonMovement == null && this.data.pokemonMovement == null) || variant.pokemonMovement.equals(this.data.pokemonMovement)));
		this.soundCheckbox.setSelected(!((variant.sound == null && this.data.sound == null) || variant.sound.equals(this.data.sound)));
		this.soundDelayCheckbox.setSelected(variant.soundDelay != this.data.soundDelay);
		this.spriteDurationCheckbox.setSelected(variant.spriteDuration != this.data.spriteDuration);
		this.widthCheckbox.setSelected(variant.width != this.data.width);
		this.heightCheckbox.setSelected(variant.height != this.data.height);
		this.xCheckbox.setSelected(variant.gravityX != this.data.gravityX);
		this.yCheckbox.setSelected(variant.gravityY != this.data.gravityY);
		this.spritesCheckbox.setSelected(
				!((variant.pokemonMovement == null && this.data.pokemonMovement == null) || variant.pokemonMovement.equals(this.data.pokemonMovement)));

		this.stateCheckbox.setSelected(variant.pokemonState != this.data.pokemonState);
		this.noStateRadio.setSelected(variant.pokemonState == null);
		this.hasStateRadio.setSelected(variant.pokemonState != null);
	}

}

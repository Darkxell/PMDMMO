package fr.darkxell.dataeditor.application.controller.animation;

import java.util.Arrays;

import com.darkxell.client.mechanics.animation.AnimationData;
import com.darkxell.common.util.Direction;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;

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
	public CheckBox playsforeachtargetCheckbox;
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
	public Direction variant;
	@FXML
	public CheckBox widthCheckbox, heightCheckbox;
	@FXML
	public CheckBox xCheckbox, yCheckbox;

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

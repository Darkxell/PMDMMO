package fr.darkxell.dataeditor.application.controller.animation;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.animation.AnimationData;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.Animations.AnimationGroup;
import com.darkxell.client.mechanics.animation.SpritesetAnimation.BackSpriteUsage;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;

import fr.darkxell.dataeditor.application.util.AnimationListItem;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;

public class EditAnimationController implements Initializable
{

	@FXML
	public TextField alsoplayDelayTextfield;
	@FXML
	public TextField alsoplayTextfield;
	@FXML
	public ComboBox<String> animMovementCombobox;
	@FXML
	public ComboBox<BackSpriteUsage> backspritesCombobox;
	@FXML
	public TextField clonesTextfield;
	@FXML
	public TextField delayTextfield;
	@FXML
	public TextField loopTextfield;
	@FXML
	public RadioButton noSpritesRadio, defaultSpritesRadio, customSpritesRadio;
	@FXML
	public TextField orderTextfield;
	@FXML
	public TextField overlayTextfield;
	@FXML
	public CheckBox playforeachtargetCheckbox;
	@FXML
	public ComboBox<String> pokemonMovementCombobox;
	@FXML
	public TextField soundDelayTextfield;
	@FXML
	public TextField soundTextfield;
	@FXML
	public TextField spriteDurationTextfield;
	@FXML
	public TextField spritesTextfield;
	@FXML
	public CheckBox stateCheckbox;
	@FXML
	public ComboBox<PokemonSpriteState> stateCombobox;
	@FXML
	public TextField stateDelayTextfield;
	@FXML
	public TextField widthTextfield, heightTextfield;
	@FXML
	public TextField xTextfield, yTextfield;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		ToggleGroup spritesGroup = new ToggleGroup();
		this.noSpritesRadio.setToggleGroup(spritesGroup);
		this.defaultSpritesRadio.setToggleGroup(spritesGroup);
		this.customSpritesRadio.setToggleGroup(spritesGroup);

		this.noSpritesRadio.setSelected(true);
		this.spritesTextfield.setDisable(true);

		this.backspritesCombobox.getItems().addAll(BackSpriteUsage.values());
		this.backspritesCombobox.getSelectionModel().select(0);

		this.stateCombobox.getItems().addAll(PokemonSpriteState.values());
		this.stateCombobox.getSelectionModel().select(0);

		this.pokemonMovementCombobox.getItems().addAll("none", "dash", "2tiles");
		this.pokemonMovementCombobox.getSelectionModel().select(0);

		this.animMovementCombobox.getItems().addAll("none", "1tilefacing", "diagonal", "upanddown", "straight (projectile)", "arc (projectile)");
		this.animMovementCombobox.getSelectionModel().select(0);

		this.stateCombobox.setDisable(true);

		Pattern pattern = Pattern.compile("\\d*");
		this.delayTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		}));
		this.loopTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		}));
		this.overlayTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		}));
		this.soundDelayTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		}));
		this.spriteDurationTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		}));
		this.stateDelayTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		}));
		this.widthTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		}));
		this.heightTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		}));
		this.xTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		}));
		this.yTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		}));

		Pattern p2 = Pattern.compile("(\\d+,)*(\\d+)?");
		this.alsoplayDelayTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return p2.matcher(change.getControlNewText()).matches() ? change : null;
		}));
		Pattern p3 = Pattern.compile("(-?\\d+,)*(-?\\d+)?");
		this.orderTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return p3.matcher(change.getControlNewText()).matches() ? change : null;
		}));
	}

	public void onCancelChanges()
	{
		this.setupFor(AnimationsTabController.instance.editing);
	}

	public void onChangeID()
	{
		AnimationsTabController.instance.onChangeID();
	}

	public void onSave()
	{
		AnimationData anim = this.update(Animations.getData(AnimationsTabController.instance.editing.id, AnimationsTabController.instance.editing.group));
		this.updateVariants(anim);
		if (anim != null) AnimationsTabController.instance.onEdited(anim);
	}

	public void onSpritesChange()
	{
		this.spritesTextfield.setDisable(!this.customSpritesRadio.isSelected());
	}

	public void onStateChange()
	{
		this.stateCombobox.setDisable(!this.stateCheckbox.isSelected());
	}

	public void setupFor(AnimationListItem item)
	{
		AnimationData data = Animations.getData(item.id, item.group);
		String apdelay = "";
		for (int i = 0; i < data.alsoPlayDelay.length; ++i)
		{
			if (i != 0) apdelay += ",";
			apdelay += data.alsoPlayDelay[i];
		}
		this.alsoplayDelayTextfield.setText(apdelay);
		String ap = "";
		for (int i = 0; i < data.alsoPlay.length; ++i)
		{
			if (i != 0) ap += ",";
			ap += data.alsoPlay[i];
		}
		this.alsoplayTextfield.setText(ap);
		this.animMovementCombobox.getSelectionModel().select(0);
		if (data.animationMovement != null) this.animMovementCombobox.setValue(data.animationMovement);
		this.backspritesCombobox.setValue(data.backSpriteUsage);
		if (data.clones != null) this.clonesTextfield.setText(data.clones);
		this.delayTextfield.setText(String.valueOf(data.delayTime));
		this.loopTextfield.setText(String.valueOf(data.loopsFrom));
		if (data.spriteOrder == null) this.orderTextfield.setText("");
		else
		{
			String order = "";
			for (int i = 0; i < data.spriteOrder.length; ++i)
			{
				if (i != 0) order += ",";
				order += data.spriteOrder[i];
			}
			this.orderTextfield.setText(order);
		}
		this.playforeachtargetCheckbox.setSelected(data.playsForEachTarget);
		this.pokemonMovementCombobox.getSelectionModel().select(0);
		if (data.pokemonMovement != null) this.pokemonMovementCombobox.setValue(data.pokemonMovement);
		this.soundDelayTextfield.setText(String.valueOf(data.soundDelay));
		if (data.sound != null) this.soundTextfield.setText(data.sound);
		this.spriteDurationTextfield.setText(String.valueOf(data.spriteDuration));
		this.stateCheckbox.setSelected(data.pokemonState != null);
		if (data.pokemonState != null) this.stateCombobox.setValue(data.pokemonState);
		this.stateDelayTextfield.setText(String.valueOf(data.pokemonStateDelay));
		this.widthTextfield.setText(String.valueOf(data.width));
		this.heightTextfield.setText(String.valueOf(data.height));
		this.xTextfield.setText(String.valueOf(data.gravityX));
		this.yTextfield.setText(String.valueOf(data.gravityY));

		if (data.sprites == null)
		{
			this.noSpritesRadio.setSelected(true);
			this.spritesTextfield.setText("");
		} else if (data.sprites.equals("" + data.id))
		{
			this.defaultSpritesRadio.setSelected(true);
			this.spritesTextfield.setText("");
		} else
		{
			this.customSpritesRadio.setSelected(true);
			this.spritesTextfield.setText(data.sprites);
		}

	}

	private AnimationData update(AnimationData data)
	{
		String in = this.alsoplayTextfield.getText();
		if (!in.equals(""))
		{
			String[] ids = in.split(",");
			data.alsoPlay = new String[ids.length];
			System.arraycopy(ids, 0, data.alsoPlay, 0, ids.length);
		} else data.alsoPlay = new String[0];

		in = this.alsoplayDelayTextfield.getText();
		if (in.equals(""))
		{
			data.alsoPlayDelay = new int[data.alsoPlay.length];
			for (int i = 0; i < data.alsoPlayDelay.length; ++i)
				data.alsoPlayDelay[i] = 0;
		} else
		{
			String[] delays = in.split(",");
			data.alsoPlayDelay = new int[delays.length];
			for (int i = 0; i < delays.length; ++i)
				try
				{
					data.alsoPlayDelay[i] = Integer.parseInt(delays[i]);
				} catch (NumberFormatException e)
				{
					FXUtils.showAlert("Wrong delay: " + delays[i]);
					return null;
				}
		}

		if (!this.animMovementCombobox.getValue().equals("none")) data.animationMovement = this.animMovementCombobox.getValue();
		data.backSpriteUsage = this.backspritesCombobox.getValue();
		if (!this.clonesTextfield.getText().equals("")) data.clones = this.clonesTextfield.getText();
		if (!this.delayTextfield.getText().equals("")) data.delayTime = Integer.parseInt(this.delayTextfield.getText());
		if (!this.loopTextfield.getText().equals("")) data.loopsFrom = Integer.parseInt(this.loopTextfield.getText());

		in = this.orderTextfield.getText();
		if (!in.equals(""))
		{
			String[] indexes = in.split(",");
			data.spriteOrder = new int[indexes.length];
			for (int i = 0; i < indexes.length; ++i)
				try
				{
					data.spriteOrder[i] = Integer.parseInt(indexes[i]);
				} catch (NumberFormatException e)
				{
					FXUtils.showAlert("Wrong sprite index: " + indexes[i]);
					return null;
				}
		} else data.spriteOrder = new int[0];

		if (!this.overlayTextfield.getText().equals("")) data.overlay = Integer.parseInt(this.overlayTextfield.getText());
		else data.overlay = -1;
		data.playsForEachTarget = this.playforeachtargetCheckbox.isSelected();
		if (!this.pokemonMovementCombobox.getValue().equals("none")) data.pokemonMovement = this.pokemonMovementCombobox.getValue();
		else data.pokemonMovement = null;
		if (!this.soundDelayTextfield.getText().equals("")) data.soundDelay = Integer.parseInt(this.soundDelayTextfield.getText());
		else data.soundDelay = 0;
		if (!this.soundTextfield.getText().equals("")) data.sound = this.soundTextfield.getText();
		else data.sound = null;
		if (!this.spriteDurationTextfield.getText().equals("")) data.spriteDuration = Integer.parseInt(this.spriteDurationTextfield.getText());
		else data.spriteDuration = 2;
		if (this.stateCheckbox.isSelected()) data.pokemonState = this.stateCombobox.getValue();
		else data.pokemonState = null;
		if (!this.stateDelayTextfield.getText().equals("")) data.pokemonStateDelay = Integer.parseInt(this.stateDelayTextfield.getText());
		else data.pokemonStateDelay = 0;
		if (!this.widthTextfield.getText().equals("")) data.width = Integer.parseInt(this.widthTextfield.getText());
		else data.width = 0;
		if (!this.heightTextfield.getText().equals("")) data.height = Integer.parseInt(this.heightTextfield.getText());
		else data.height = 0;
		if (!this.xTextfield.getText().equals("")) data.gravityX = Integer.parseInt(this.xTextfield.getText());
		else data.gravityX = -1;
		if (!this.yTextfield.getText().equals("")) data.gravityY = Integer.parseInt(this.yTextfield.getText());
		else data.gravityY = -1;

		if (this.noSpritesRadio.isSelected()) data.sprites = null;
		else if (this.defaultSpritesRadio.isSelected()) data.sprites = "" + data.id;
		else data.sprites = this.spritesTextfield.getText();

		return data;
	}

	private void updateVariant(AnimationData anim, AnimationData previous, AnimationData variant)
	{
		if (Arrays.equals(variant.alsoPlay, previous.alsoPlay)) variant.alsoPlay = anim.alsoPlay;
		if (Arrays.equals(variant.alsoPlayDelay, previous.alsoPlayDelay)) variant.alsoPlayDelay = anim.alsoPlayDelay;
		if ((variant.animationMovement == null && previous.animationMovement == null) || (variant.animationMovement.equals(previous.animationMovement)))
			variant.animationMovement = anim.animationMovement;
		if (variant.backSpriteUsage == previous.backSpriteUsage) variant.backSpriteUsage = anim.backSpriteUsage;
		if ((variant.clones == null && previous.clones == null) || (variant.clones.equals(previous.clones))) variant.clones = anim.clones;
		if (variant.delayTime == previous.delayTime) variant.delayTime = anim.delayTime;
		if (variant.loopsFrom == previous.loopsFrom) variant.loopsFrom = anim.loopsFrom;
		if (Arrays.equals(variant.spriteOrder, previous.spriteOrder)) variant.spriteOrder = anim.spriteOrder;
		if (variant.overlay == previous.overlay) variant.overlay = anim.overlay;
		if (variant.playsForEachTarget == previous.playsForEachTarget) variant.playsForEachTarget = anim.playsForEachTarget;
		if ((variant.pokemonMovement == null && previous.pokemonMovement == null) || (variant.pokemonMovement.equals(previous.pokemonMovement)))
			variant.pokemonMovement = anim.pokemonMovement;
		if ((variant.sound == null && previous.sound == null) || (variant.sound.equals(previous.sound))) variant.sound = anim.sound;
		if (variant.soundDelay == previous.soundDelay) variant.soundDelay = anim.soundDelay;
		if (variant.spriteDuration == previous.spriteDuration) variant.spriteDuration = anim.spriteDuration;
		if ((variant.sprites == null && previous.sprites == null) || (variant.sprites.equals(previous.sprites))) variant.sprites = anim.sprites;
		if (variant.pokemonState == previous.pokemonState) variant.pokemonState = anim.pokemonState;
		if (variant.pokemonStateDelay == previous.pokemonStateDelay) variant.pokemonStateDelay = anim.pokemonStateDelay;
		if (variant.width == previous.width) variant.width = anim.width;
		if (variant.height == previous.height) variant.height = anim.height;
		if (variant.gravityX == previous.gravityX) variant.gravityX = anim.gravityX;
		if (variant.gravityY == previous.gravityY) variant.gravityY = anim.gravityY;
	}

	private void updateVariants(AnimationData anim)
	{
		AnimationGroup group = AnimationsTabController.instance.editing.group;
		AnimationData previous = Animations.getData(anim.id, group);

		for (int i = 0; i < previous.variants.length; ++i)
		{
			anim.variants[i] = previous.variants[i];
			this.updateVariant(anim, previous, anim.variants[i]); // TODO this doesn't work yet :/
		}
	}

}

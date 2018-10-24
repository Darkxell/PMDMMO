package fr.darkxell.dataeditor.application.controller.animation;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.animation.SpritesetAnimation.BackSpriteUsage;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;

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
		TextFormatter<String> formatter2 = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return p2.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.alsoplayDelayTextfield.setTextFormatter(formatter2);
	}

	public void onCancelChanges()
	{}

	public void onSave()
	{}

	public void onSpritesChange()
	{
		this.spritesTextfield.setDisable(!this.customSpritesRadio.isSelected());
	}

	public void onStateChange()
	{
		this.stateCombobox.setDisable(!this.stateCheckbox.isSelected());
	}

}

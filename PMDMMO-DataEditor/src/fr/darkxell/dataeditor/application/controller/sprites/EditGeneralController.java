package fr.darkxell.dataeditor.application.controller.sprites;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.darkxell.client.resources.images.RegularSpriteSet;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesetData;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class EditGeneralController implements Initializable {

	@FXML
	public CheckBox bigShadowCheckbox;
	@FXML
	public TextField heightTextfield;
	public SpritesTabController parent;
	public RegularSpriteSet spriteset;
	private int spritesetid;
	@FXML
	public TextField widthTextfield;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Pattern p = Pattern.compile("\\d*");
		this.heightTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return p.matcher(change.getControlNewText()).matches() ? change : null;
		}));
		this.widthTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return p.matcher(change.getControlNewText()).matches() ? change : null;
		}));

		this.heightTextfield.textProperty().addListener((observable, oldValue, newValue) -> this.onDimensionsChanged());
		this.widthTextfield.textProperty().addListener((observable, oldValue, newValue) -> this.onDimensionsChanged());
	}

	public void onCancelChanges() {
		this.parent.onEdit(this.parent.currentSprite);
	}

	private void onDimensionsChanged() {
		int width = 16, height = 16;
		if (!this.widthTextfield.getText().equals("")) width = Integer.parseInt(this.widthTextfield.getText());
		if (!this.heightTextfield.getText().equals("")) height = Integer.parseInt(this.heightTextfield.getText());
		this.spriteset = PokemonSpritesets.loadTestSpriteset(this.spritesetid, width, height);
		this.parent.onDimensionsChanged();
	}

	public void onSaveChanges() {
		if (this.parent.sequencesController.sequenceCombobox.getValue() != null)
			this.parent.sequencesController.saveSequence(this.parent.sequencesController.sequenceCombobox.getValue());
		this.parent.onSaveChanges();
	}

	public void setupFor(PokemonSpritesetData item) {
		this.spritesetid = item.id;
		this.widthTextfield.setText("");
		this.heightTextfield.setText("");
		this.widthTextfield.setText(String.valueOf(item.spriteWidth));
		this.heightTextfield.setText(String.valueOf(item.spriteHeight));
		this.bigShadowCheckbox.setSelected(item.hasBigShadow);

		this.onDimensionsChanged();
	}

}

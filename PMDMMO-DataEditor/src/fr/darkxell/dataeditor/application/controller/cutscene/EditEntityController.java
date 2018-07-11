package fr.darkxell.dataeditor.application.controller.cutscene;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.Direction;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class EditEntityController implements Initializable
{

	@FXML
	private CheckBox animatedCheckbox;
	@FXML
	private ComboBox<String> entityTypeCombobox;
	@FXML
	private ComboBox<Direction> facingCombobox;
	@FXML
	private TextField idTextfield;
	@FXML
	private TextField memberTextfield;
	@FXML
	private ComboBox<String> modeCombobox;
	@FXML
	private ComboBox<PokemonSpecies> speciesCombobox;
	@FXML
	private ComboBox<PokemonSpriteState> stateCombobox;
	@FXML
	private TextField xposTextfield;
	@FXML
	private TextField yposTextfield;

	private CutsceneEntity getEntity()
	{
		CutsceneEntity e;
		if (this.entityTypeCombobox.getSelectionModel().getSelectedIndex() == 0)
		{
			e = new CutsceneEntity(Integer.parseInt(this.idTextfield.getText()), Integer.parseInt(this.xposTextfield.getText()),
					Integer.parseInt(this.yposTextfield.getText()));
		} else
		{
			Pokemon p;
			if (this.modeCombobox.getSelectionModel().getSelectedIndex() == 0)
				p = Persistance.player.getMember(Integer.parseInt(this.memberTextfield.getText()));
			else p = this.speciesCombobox.getSelectionModel().getSelectedItem().generate(new Random(), 1);
			e = new CutscenePokemon(p, this.stateCombobox.getSelectionModel().getSelectedItem(), this.facingCombobox.getSelectionModel().getSelectedItem(),
					this.animatedCheckbox.isSelected());
		}
		return e;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.entityTypeCombobox.getItems().addAll("Default", "Pokémon");
		this.entityTypeCombobox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
			modeCombobox.setDisable(newValue.equals(0));
			speciesCombobox.setDisable(newValue.equals(0));
			memberTextfield.setDisable(newValue.equals(0));
			stateCombobox.setDisable(newValue.equals(0));
			facingCombobox.setDisable(newValue.equals(0));
			animatedCheckbox.setDisable(newValue.equals(0));
		});
		this.entityTypeCombobox.getSelectionModel().select(0);

		this.modeCombobox.getItems().addAll("Pokémon species", "Team member");
		this.modeCombobox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
			speciesCombobox.setVisible(newValue.equals(0));
			memberTextfield.setVisible(newValue.equals(1));
		});
		this.modeCombobox.getSelectionModel().select(0);

		this.speciesCombobox.getItems().addAll(PokemonRegistry.list());
		this.speciesCombobox.getSelectionModel().select(1);

		this.stateCombobox.getItems().addAll(PokemonSpriteState.values());
		this.stateCombobox.getSelectionModel().select(0);

		this.facingCombobox.getItems().addAll(Direction.directions);
		this.facingCombobox.getSelectionModel().select(0);

		Pattern pattern = Pattern.compile("-?\\d*");
		TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.xposTextfield.setTextFormatter(formatter);
		formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.yposTextfield.setTextFormatter(formatter);
		formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return Pattern.compile("\\d*").matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.idTextfield.setTextFormatter(formatter);
		formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return Pattern.compile("(0|1|2|3)?").matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.memberTextfield.setTextFormatter(formatter);
	}

	public void onCancel()
	{
		CutsceneCreationController.editEntityPopup.close();
	}

	public void onOk()
	{
		this.onCancel();
		CutscenesTabController.instance.editCutsceneController.cutsceneCreationController.entitiesList.getItems().add(this.getEntity());
	}

}

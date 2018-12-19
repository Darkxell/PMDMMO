package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import com.darkxell.common.Registries;
import com.darkxell.common.dungeon.data.DungeonEncounter;
import com.darkxell.common.pokemon.PokemonSpecies;

import fr.darkxell.dataeditor.application.data.DungeonEncounterTableItem;
import fr.darkxell.dataeditor.application.util.DungeonCreationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class EditEncounterController implements Initializable
{

	public DungeonEncounterTableItem editing;

	@FXML
	public EditFloorsetController floorsetController;
	@FXML
	public TextField levelTextfield;
	@FXML
	public ComboBox<PokemonSpecies> pokemonCombobox;
	@FXML
	public TextField weightTextfield;

	private DungeonEncounter generate() throws DungeonCreationException
	{
		if (!this.levelTextfield.getText().matches("\\d+")) throw new DungeonCreationException("Invalid Level: " + this.levelTextfield.getText());
		if (!this.weightTextfield.getText().matches("\\d+")) throw new DungeonCreationException("Invalid Weight: " + this.weightTextfield.getText());

		return new DungeonEncounter(this.pokemonCombobox.getValue().id, Integer.parseInt(this.levelTextfield.getText()),
				Integer.parseInt(this.weightTextfield.getText()), this.floorsetController.generate());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.pokemonCombobox.getItems().addAll(Registries.species().toList());
		this.pokemonCombobox.getItems().sort(Comparator.naturalOrder());
		this.pokemonCombobox.getSelectionModel().select(0);
	}

	public void onSave()
	{
		try
		{
			this.editing.encounter = this.generate();
			EditDungeonPokemonController.instance.onPokemonEdited(this.editing);
		} catch (DungeonCreationException e)
		{
			new Alert(AlertType.ERROR, "There was an error while saving the Dungeon: " + e.getMessage() + ".", ButtonType.OK).showAndWait();
		}
	}

	public void setupFor(DungeonEncounterTableItem encounter)
	{
		this.editing = encounter;
		if (encounter != null)
		{
			this.pokemonCombobox.setValue(encounter.getPokemon());
			this.levelTextfield.setText(String.valueOf(encounter.getLevel()));
			this.weightTextfield.setText(String.valueOf(encounter.getWeight()));
			this.floorsetController.setupFor(encounter.getFloors());
		} else this.floorsetController.setupFor(null);
	}

}

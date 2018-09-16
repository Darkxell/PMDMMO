package fr.darkxell.dataeditor.application.controller.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.data.Dungeon;

import fr.darkxell.dataeditor.application.util.DungeonCreationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class EditDungeonController
{

	@FXML
	public EditDungeonBuriedItemsController dungeonBuriedItemsTabController;
	@FXML
	public EditDungeonDataController dungeonDataTabController;
	@FXML
	public EditDungeonFloorController dungeonFloorTabController;
	@FXML
	public EditDungeonItemsController dungeonItemsTabController;
	@FXML
	public EditDungeonPokemonController dungeonPokemonTabController;
	@FXML
	public EditDungeonShopItemsController dungeonShopItemsTabController;
	@FXML
	public EditDungeonTrapsController dungeonTrapsTabController;
	@FXML
	public EditDungeonWeatherController dungeonWeatherTabController;

	public void cancelChanges()
	{
		this.setupFor(DungeonsTabController.instance.currentDungeon);
	}

	public Dungeon generateDungeon() throws DungeonCreationException
	{
		return this.dungeonDataTabController.generateDungeon(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
				new ArrayList<>(), this.dungeonWeatherTabController.generate());
	}

	public void saveChanges()
	{
		try
		{
			DungeonsTabController.instance.onEdited(this.generateDungeon());
		} catch (DungeonCreationException e)
		{
			new Alert(AlertType.ERROR, "There was an error while saving the Dungeon: " + e.getMessage() + ".", ButtonType.OK).showAndWait();
		}
	}

	public void setupFor(Dungeon dungeon)
	{
		this.dungeonDataTabController.setupFor(dungeon);
		this.dungeonFloorTabController.setupFor(dungeon);
		this.dungeonPokemonTabController.setupFor(dungeon);
		this.dungeonItemsTabController.setupFor(dungeon);
		this.dungeonShopItemsTabController.setupFor(dungeon);
		this.dungeonBuriedItemsTabController.setupFor(dungeon);
		this.dungeonTrapsTabController.setupFor(dungeon);
		this.dungeonWeatherTabController.setupFor(dungeon);
	}

}

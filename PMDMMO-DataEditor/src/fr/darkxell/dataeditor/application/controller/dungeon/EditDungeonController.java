package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.common.dungeon.data.Dungeon;

import fr.darkxell.dataeditor.application.util.DungeonCreationException;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class EditDungeonController implements Initializable
{

	@FXML
	public EditDungeonItemsController dungeonBuriedItemsTabController;
	@FXML
	public EditDungeonDataController dungeonDataTabController;
	@FXML
	public EditDungeonFloorController dungeonFloorTabController;
	@FXML
	public EditDungeonItemsController dungeonItemsTabController;
	@FXML
	public EditDungeonPokemonController dungeonPokemonTabController;
	@FXML
	public EditDungeonItemsController dungeonShopItemsTabController;
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
		return this.dungeonDataTabController.generateDungeon(this.dungeonPokemonTabController.generate(), this.dungeonItemsTabController.generate(),
				this.dungeonShopItemsTabController.generate(), this.dungeonBuriedItemsTabController.generate(), this.dungeonTrapsTabController.generate(),
				this.dungeonFloorTabController.generate(), this.dungeonWeatherTabController.generate());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.dungeonBuriedItemsTabController.itemType = EditDungeonItemsController.BURIED;
		this.dungeonShopItemsTabController.itemType = EditDungeonItemsController.SHOP;
	}

	public void saveChanges()
	{
		try
		{
			DungeonsTabController.instance.onEdited(this.generateDungeon());
		} catch (DungeonCreationException e)
		{
			FXUtils.showAlert("There was an error while saving the Dungeon: " + e.getMessage() + ".");
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

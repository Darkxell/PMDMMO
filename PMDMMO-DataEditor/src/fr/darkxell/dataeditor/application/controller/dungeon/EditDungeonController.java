package fr.darkxell.dataeditor.application.controller.dungeon;

import com.darkxell.common.dungeon.data.Dungeon;

import javafx.fxml.FXML;

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

	public Dungeon generateDungeon()
	{
		return DungeonsTabController.instance.defaultDungeon(DungeonsTabController.instance.currentDungeon.id);
	}

	public void saveChanges()
	{
		DungeonsTabController.instance.onEdited(this.generateDungeon());
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

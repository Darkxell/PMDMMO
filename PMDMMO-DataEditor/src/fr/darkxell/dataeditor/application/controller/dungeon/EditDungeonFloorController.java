package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.PokemonType;

import fr.darkxell.dataeditor.application.controls.FloorTable;
import fr.darkxell.dataeditor.application.data.floortable.FloorTableItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

public class EditDungeonFloorController implements Initializable
{

	@FXML
	public TableView<FloorTableItem<Boolean, String>> bossTable;
	@FXML
	public TableView<FloorTableItem<Integer, String>> buriedTable;
	@FXML
	public TableView<FloorTableItem<PokemonType, PokemonType>> camouflageTable;
	@FXML
	public TableView<FloorTableItem<Integer, String>> difficultyTable;
	@FXML
	public TableView<FloorTableItem<Integer, String>> itemTable;
	@FXML
	public TableView<FloorTableItem<Integer, String>> layoutTable;
	@FXML
	public TableView<FloorTableItem<Integer, String>> moneyTable;
	@FXML
	public TableView<FloorTableItem<Integer, String>> monsterHouseTable;
	@FXML
	public TableView<FloorTableItem<Move, Move>> naturePowerTable;
	@FXML
	public TableView<FloorTableItem<Integer, String>> pokemonTable;
	@FXML
	public TableView<FloorTableItem<String, String>> secretPowerTable;
	@FXML
	public TableView<FloorTableItem<Integer, String>> shadowsTable;
	@FXML
	public TableView<FloorTableItem<Integer, String>> shopTable;
	@FXML
	public TableView<FloorTableItem<Integer, String>> soundtrackTable;
	@FXML
	public TableView<FloorTableItem<Integer, String>> terrainTable;
	@FXML
	public TableView<FloorTableItem<Integer, String>> trapTable;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		FloorTable.setup(this.difficultyTable, "Difficulty", FloorTableItem.defaultInteger);
		FloorTable.setup(this.moneyTable, "Base Money", FloorTableItem.defaultInteger);
		FloorTable.setup(this.layoutTable, "Layout", FloorTableItem.defaultInteger);
		FloorTable.setup(this.terrainTable, "Terrain", FloorTableItem.defaultInteger);
		FloorTable.setup(this.shadowsTable, "Shadows (0-2)", FloorTableItem.defaultInteger);
		FloorTable.setup(this.camouflageTable, "Camouflage", FloorTableItem.defaultPokemonType);
		FloorTable.setup(this.naturePowerTable, "Nature Power", FloorTableItem.defaultMove);
		FloorTable.setup(this.secretPowerTable, "Secret Power", FloorTableItem.defaultString);
		FloorTable.setup(this.soundtrackTable, "Soundtrack", FloorTableItem.defaultInteger);
		FloorTable.setup(this.shopTable, "Shops", FloorTableItem.defaultInteger);
		FloorTable.setup(this.monsterHouseTable, "Monster Houses", FloorTableItem.defaultInteger);
		FloorTable.setup(this.itemTable, "Items", FloorTableItem.defaultInteger);
		FloorTable.setup(this.pokemonTable, "Pokemon", FloorTableItem.defaultInteger);
		FloorTable.setup(this.trapTable, "Traps", FloorTableItem.defaultInteger);
		FloorTable.setup(this.buriedTable, "Buried Items", FloorTableItem.defaultInteger);
		FloorTable.setup(this.bossTable, "Boss?", FloorTableItem.defaultBoolean);
	}

	public void setupFor(Dungeon dungeon)
	{
		// TODO Auto-generated method stub
	}

}

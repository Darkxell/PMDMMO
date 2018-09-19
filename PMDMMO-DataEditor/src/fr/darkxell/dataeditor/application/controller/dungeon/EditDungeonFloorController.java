package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.PokemonType;

import fr.darkxell.dataeditor.application.controls.FloorTable;
import fr.darkxell.dataeditor.application.data.floortable.FloorTableBoolean;
import fr.darkxell.dataeditor.application.data.floortable.FloorTableInteger;
import fr.darkxell.dataeditor.application.data.floortable.FloorTableItem;
import fr.darkxell.dataeditor.application.data.floortable.FloorTableMove;
import fr.darkxell.dataeditor.application.data.floortable.FloorTablePokemonType;
import fr.darkxell.dataeditor.application.data.floortable.FloorTableString;
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
		this.difficultyTable.getItems().clear();
		this.moneyTable.getItems().clear();
		this.layoutTable.getItems().clear();
		this.terrainTable.getItems().clear();
		this.shadowsTable.getItems().clear();
		this.camouflageTable.getItems().clear();
		this.naturePowerTable.getItems().clear();
		this.secretPowerTable.getItems().clear();
		this.soundtrackTable.getItems().clear();
		this.shopTable.getItems().clear();
		this.monsterHouseTable.getItems().clear();
		this.itemTable.getItems().clear();
		this.pokemonTable.getItems().clear();
		this.trapTable.getItems().clear();
		this.bossTable.getItems().clear();

		FloorData previous = null;
		for (int floor = 1; floor <= dungeon.floorCount; ++floor)
		{
			FloorData current = dungeon.getData(floor);
			if (previous != current)
			{
				if (previous == null || previous.difficulty() != current.difficulty())
					this.difficultyTable.getItems().add(new FloorTableInteger(floor, current.difficulty()));
				if (previous == null || previous.baseMoney() != current.baseMoney())
					this.moneyTable.getItems().add(new FloorTableInteger(floor, current.baseMoney()));
				if (previous == null || previous.layoutID() != current.layoutID())
					this.layoutTable.getItems().add(new FloorTableInteger(floor, current.layoutID()));
				if (previous == null || previous.terrainSpriteset() != current.terrainSpriteset())
					this.terrainTable.getItems().add(new FloorTableInteger(floor, current.terrainSpriteset()));
				if (previous == null || previous.shadows() != current.shadows())
					this.shadowsTable.getItems().add(new FloorTableInteger(floor, (int) current.shadows()));
				if (previous == null || previous.camouflageType() != current.camouflageType())
					this.camouflageTable.getItems().add(new FloorTablePokemonType(floor, current.camouflageType()));
				if (previous == null || previous.naturePowerID() != current.naturePowerID())
					this.naturePowerTable.getItems().add(new FloorTableMove(floor, current.naturePower()));
				if (previous == null || (previous.secretPower() != null && !previous.secretPower().equals(current.secretPower()))
						|| (current.secretPower() != null && !current.secretPower().equals(previous.secretPower())))
					this.secretPowerTable.getItems().add(new FloorTableString(floor, current.secretPower()));
				if (previous == null || previous.soundtrack() != current.soundtrack())
					this.soundtrackTable.getItems().add(new FloorTableInteger(floor, current.soundtrack()));
				if (previous == null || previous.shopChance() != current.shopChance())
					this.shopTable.getItems().add(new FloorTableInteger(floor, (int) current.shopChance()));
				if (previous == null || previous.monsterHouseChance() != current.monsterHouseChance())
					this.monsterHouseTable.getItems().add(new FloorTableInteger(floor, (int) current.monsterHouseChance()));
				if (previous == null || previous.itemDensity() != current.itemDensity())
					this.itemTable.getItems().add(new FloorTableInteger(floor, (int) current.itemDensity()));
				if (previous == null || previous.pokemonDensity() != current.pokemonDensity())
					this.pokemonTable.getItems().add(new FloorTableInteger(floor, (int) current.pokemonDensity()));
				if (previous == null || previous.trapDensity() != current.trapDensity())
					this.trapTable.getItems().add(new FloorTableInteger(floor, (int) current.trapDensity()));
				if (previous == null || previous.isBossFloor() != current.isBossFloor())
					this.bossTable.getItems().add(new FloorTableBoolean(floor, current.isBossFloor()));
			}
			previous = current;
		}
	}

}

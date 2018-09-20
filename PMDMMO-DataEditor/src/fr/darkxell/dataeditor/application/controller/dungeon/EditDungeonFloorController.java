package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;
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

	private static <T, D> T lookup(TableView<FloorTableItem<T, D>> table, int floor)
	{
		for (FloorTableItem<T, D> item : table.getItems())
			if (item.floor == floor) return item.value;
		return null;
	}

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

	public ArrayList<FloorData> generate()
	{
		int max = 0;

		ArrayList<FloorData> data = new ArrayList<>();
		int diffP = 0, moneyP = 0, layoutP = 0, terrainP = 0, shadowsP = 0, soundtrackP = 0, shopP = 0, monsterP = 0, itemP = 0, pokemonP = 0, trapP = 0,
				buriedP = 0;
		int diff = diffP, money = moneyP, layout = layoutP, terrain = terrainP, shadows = shadowsP, soundtrack = soundtrackP, shop = shopP, monster = monsterP,
				item = itemP, pokemon = pokemonP, trap = trapP, buried = buriedP;
		PokemonType camouflage = PokemonType.Unknown, camouflageP = PokemonType.Unknown;
		Move nature = MoveRegistry.ATTACK, natureP = MoveRegistry.ATTACK;
		String secret = null, secretP = null;
		boolean boss = false, bossP = false;
		boolean hasChanged;
		int startFloor = -1;

		for (int floor = 1; floor <= max; ++floor)
		{
			diff = lookup(this.difficultyTable, floor);
			money = lookup(this.moneyTable, floor);
			layout = lookup(this.layoutTable, floor);
			terrain = lookup(this.terrainTable, floor);
			shadows = lookup(this.shadowsTable, floor);
			camouflage = lookup(this.camouflageTable, floor);
			nature = lookup(this.naturePowerTable, floor);
			secret = lookup(this.secretPowerTable, floor);
			soundtrack = lookup(this.soundtrackTable, floor);
			shop = lookup(this.shopTable, floor);
			monster = lookup(this.monsterHouseTable, floor);
			item = lookup(this.itemTable, floor);
			pokemon = lookup(this.pokemonTable, floor);
			trap = lookup(this.trapTable, floor);
			buried = lookup(this.buriedTable, floor);
			boss = lookup(this.bossTable, floor);

			hasChanged = diff != diffP;
			hasChanged |= money != moneyP;
			hasChanged |= layout != layoutP;
			hasChanged |= terrain != terrainP;
			hasChanged |= shadows != shadowsP;
			hasChanged |= camouflage != camouflageP;
			hasChanged |= nature != natureP;
			hasChanged |= (secret == null) != (secretP == null);
			hasChanged |= secret != null && !secret.equals(secretP);
			hasChanged |= soundtrack != soundtrackP;
			hasChanged |= shop != shopP;
			hasChanged |= monster != monsterP;
			hasChanged |= item != itemP;
			hasChanged |= pokemon != pokemonP;
			hasChanged |= trap != trapP;
			hasChanged |= buried != buriedP;
			hasChanged |= boss != bossP;

			if (hasChanged)
			{
				if (startFloor != -1)
				{
					FloorData d = new FloorData(new FloorSet(startFloor, floor), diff, money, layout, terrain, (byte) shadows, camouflage, nature.id, secret,
							soundtrack, (short) shop, (short) monster, (short) item, (short) pokemon, (short) trap, (short) buried, boss);
					data.add(d);
				}
				startFloor = floor;
			}

			diffP = diff;
			moneyP = money;
			layoutP = layout;
			terrainP = terrain;
			shadowsP = shadows;
			camouflageP = camouflage;
			natureP = nature;
			secretP = secret;
			soundtrackP = soundtrack;
			shopP = shop;
			monsterP = monster;
			itemP = item;
			pokemonP = pokemon;
			trapP = trap;
			buriedP = buried;
			bossP = boss;
		}

		if (startFloor != -1)
		{
			FloorData d = new FloorData(new FloorSet(startFloor, max), diff, money, layout, terrain, (byte) shadows, camouflage, nature.id, secret, soundtrack,
					(short) shop, (short) monster, (short) item, (short) pokemon, (short) trap, (short) buried, boss);
			data.add(d);
		}

		return data;
	}

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

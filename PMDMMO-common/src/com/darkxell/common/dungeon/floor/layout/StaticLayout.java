package com.darkxell.common.dungeon.floor.layout;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jdom2.Element;

import com.darkxell.common.dbobject.DBLearnedmove;
import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.common.dungeon.floor.ComplexRoom;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Room;
import com.darkxell.common.dungeon.floor.SquareRoom;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.BaseStats;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;

public class StaticLayout extends Layout
{

	private static Pokemon readPokemon(Element xml, Random r)
	{
		ItemStack item = xml.getChild(ItemStack.XML_ROOT) == null ? null : new ItemStack(xml.getChild(ItemStack.XML_ROOT));

		DBPokemon db = new DBPokemon();
		db.id = XMLUtils.getAttribute(xml, "pk-id", 0);
		db.specieid = Integer.parseInt(xml.getAttributeValue("id"));
		db.nickname = xml.getAttributeValue("nickname");
		db.level = Integer.parseInt(xml.getAttributeValue("level"));
		PokemonSpecies species = PokemonRegistry.find(db.specieid);
		BaseStats defaultStats = species.statsForLevel(db.level);
		db.stat_atk = XMLUtils.getAttribute(xml, "atk", defaultStats.getAttack());
		db.stat_def = XMLUtils.getAttribute(xml, "def", defaultStats.getAttack());
		db.stat_hp = XMLUtils.getAttribute(xml, "hp", defaultStats.getAttack());
		db.stat_speatk = XMLUtils.getAttribute(xml, "spa", defaultStats.getAttack());
		db.stat_spedef = XMLUtils.getAttribute(xml, "spd", defaultStats.getAttack());
		db.abilityid = XMLUtils.getAttribute(xml, "ability", species.randomAbility(r));
		db.experience = XMLUtils.getAttribute(xml, "xp", 0);
		db.gender = XMLUtils.getAttribute(xml, "gender", species.randomGender(r));
		db.iq = XMLUtils.getAttribute(xml, "iq", 0);
		db.isshiny = XMLUtils.getAttribute(xml, "shiny", false);

		LearnedMove[] moves = new LearnedMove[4];
		ArrayList<Integer> learned = new ArrayList<>();
		for (Element move : xml.getChildren("move"))
		{
			DBLearnedmove dbl = new DBLearnedmove();
			dbl.addedlevel = XMLUtils.getAttribute(move, "addedlevel", 0);
			dbl.isenabled = XMLUtils.getAttribute(move, "enabled", true);
			dbl.islinked = XMLUtils.getAttribute(move, "linked", false);
			dbl.moveid = XMLUtils.getAttribute(move, "id", 1);
			dbl.slot = Integer.parseInt(move.getAttributeValue("slot"));
			if (dbl.slot < 0 || dbl.slot >= moves.length) continue;
			moves[dbl.slot] = new LearnedMove(dbl);
			learned.add(dbl.moveid);
		}

		for (int i = 0; i < moves.length; ++i)
			if (moves[i] == null)
			{
				int id = species.latestMove(db.level, learned);
				if (id == -1) break;
				moves[i] = new LearnedMove(id);
				moves[i].setSlot(i);
				learned.add(id);
			}

		Pokemon p = new Pokemon(db);
		for (int i = 0; i < moves.length; ++i)
			p.setMove(i, moves[i]);
		p.setItem(item);
		return p;
	}

	/** Temporary variable to store the XML data to use to load the Floor. */
	private Element xml;

	@Override
	public void generate(Floor floor)
	{
		this.xml = XMLUtils.read(StaticLayout.class.getResourceAsStream("/data/floors/" + floor.dungeon.id + "-" + floor.id + ".xml"));
		super.generate(floor);
		this.placeTraps();
	}

	@Override
	protected void generateLiquids()
	{}

	@Override
	protected void generatePaths()
	{}

	@Override
	protected void generateRooms()
	{
		// ROOMS
		List<Element> rooms = this.xml.getChild("rooms", xml.getNamespace()).getChildren();
		this.floor.rooms = new Room[rooms.size()];
		for (int i = 0; i < this.floor.rooms.length; ++i)
		{
			if (rooms.get(i).getName().equals("complex")) this.floor.rooms[i] = new ComplexRoom(floor, rooms.get(i));
			else this.floor.rooms[i] = new SquareRoom(this.floor, rooms.get(i));
		}

		// TILES
		List<Element> rows = xml.getChild("tiles", xml.getNamespace()).getChildren("row", xml.getNamespace());
		for (int y = 0; y < rows.size(); ++y)
		{
			String data = rows.get(y).getText();
			if (this.floor.tiles == null) this.floor.tiles = new Tile[data.length()][rows.size()];
			for (int x = 0; x < data.length(); x++)
			{
				Tile t = new Tile(this.floor, x, y, TileType.find(data.charAt(x)));
				if (t.type() == null)
				{
					Logger.e("Invalid tile type: " + data.charAt(x));
					t.setType(TileType.GROUND);
				}
				this.floor.tiles[x][y] = t;
			}
		}
	}

	@Override
	protected void placeItems()
	{
		if (this.xml.getChild("items", xml.getNamespace()) != null)
			for (Element item : this.xml.getChild("items", xml.getNamespace()).getChildren(Item.XML_ROOT, xml.getNamespace()))
			this.floor.tiles[Integer.parseInt(item.getAttributeValue("x"))][Integer.parseInt(item.getAttributeValue("y"))].setItem(new ItemStack(item));
	}

	@Override
	protected void placeStairs()
	{}

	@Override
	protected void placeTeam()
	{
		Element spawn = this.xml.getChild("spawn", xml.getNamespace());
		this.floor.teamSpawn = new Point(Integer.parseInt(spawn.getAttributeValue("x")), Integer.parseInt(spawn.getAttributeValue("y")));
	}

	@Override
	protected void placeTraps()
	{
		if (this.xml.getChild("traps", xml.getNamespace()) != null)
			for (Element trap : this.xml.getChild("traps", xml.getNamespace()).getChildren("trap", xml.getNamespace()))
			{
			Tile t = this.floor.tiles[Integer.parseInt(trap.getAttributeValue("x"))][Integer.parseInt(trap.getAttributeValue("y"))];
			t.trap = TrapRegistry.find(Integer.parseInt(trap.getAttributeValue("id")));
			if (t.trap == TrapRegistry.WONDER_TILE) t.trapRevealed = true;
			}
	}

	@Override
	protected void summonPokemon()
	{
		if (this.xml.getChild("pokemons", xml.getNamespace()) != null)
			for (Element pokemon : this.xml.getChild("pokemons", xml.getNamespace()).getChildren(Pokemon.XML_ROOT, xml.getNamespace()))
			{
			DungeonPokemon p = new DungeonPokemon(readPokemon(pokemon, this.floor.random));
			p.isBoss = pokemon.getChild("boss", xml.getNamespace()) != null;
			this.floor.summonPokemon(p, Integer.parseInt(pokemon.getAttributeValue("x")), Integer.parseInt(pokemon.getAttributeValue("y")), new ArrayList<>());
			}
	}

}

package com.darkxell.common.dungeon.floor.layout;

import java.awt.Point;
import java.io.File;
import java.util.List;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Room;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.XMLUtils;

public class StaticLayout extends Layout {

	/** Temporary variable to store the XML data to use to load the Floor. */
	private Element xml;

	@Override
	public void generate(Floor floor) {
		this.xml = XMLUtils.readFile(new File("resources/data/floors/" + floor.dungeon.id + "-" + floor.id + ".xml"));
		super.generate(floor);
	}

	@Override
	protected void generateLiquids() {
	}

	@Override
	protected void generatePaths() {
	}

	@Override
	protected void generateRooms() {
		//ROOMS
		List<Element> rooms = this.xml.getChild("rooms").getChildren("room");
		this.floor.rooms = new Room[rooms.size()];
		for (int i = 0; i < this.floor.rooms.length; ++i)
			this.floor.rooms[i] = new Room(this.floor, rooms.get(i));
		//TILES
		String[] data = xml.getChildText("tiles").split(";");
		this.floor.tiles = new Tile[data[0].length()][data.length];
		for (int y = 0; y < data.length; y++)
			for (int x = 0; x < data[y].length(); x++)
				this.floor.tiles[x][y] = new Tile(this.floor, x, y, TileType.find(data[y].charAt(x)));
	}

	@Override
	protected void placeItems() {
		for (Element item : this.xml.getChild("items").getChildren(Item.XML_ROOT))
			this.floor.tiles[Integer.parseInt(item.getAttributeValue("x"))][Integer
					.parseInt(item.getAttributeValue("y"))].setItem(new ItemStack(item));
	}

	@Override
	protected void placeStairs() {
	}

	@Override
	protected void placeTeam() {
		Element spawn = this.xml.getChild("spawn");
		this.floor.teamSpawn = new Point(Integer.parseInt(spawn.getAttributeValue("x")),
				Integer.parseInt(spawn.getAttributeValue("y")));
	}

	@Override
	protected void placeTraps() {
	}

	@Override
	protected void placeWonderTiles() {
	}

	@Override
	protected void summonPokemon() {
		if (this.xml.getChild("pokemons") != null)
			for (Element pokemon : this.xml.getChild("pokemons").getChildren(Pokemon.XML_ROOT))
				this.floor.tiles[Integer.parseInt(pokemon.getAttributeValue("x"))][Integer
						.parseInt(pokemon.getAttributeValue("y"))].setPokemon(new DungeonPokemon(new Pokemon(pokemon)));
	}

}

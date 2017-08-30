package com.darkxell.common.dungeon.floor.layout;

import java.awt.Point;
import java.io.File;
import java.util.List;

import javafx.util.Pair;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Room;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonD;
import com.darkxell.common.util.XMLUtils;

public class StaticLayout extends Layout
{

	/** Temporary variable to store the XML data to use to load the Floor. */
	private Element xml;
	/** Temporary variable to store the starting coordinates of this Layout. */
	private int xStart, yStart;

	public StaticLayout()
	{
		super(0);
	}

	@Override
	public Pair<Room[], Point> generate(Floor floor, Tile[][] tiles)
	{
		this.xml = XMLUtils.readFile(new File("resources/data/floors/" + floor.dungeon.id + "-" + floor.id + ".xml"));
		Pair<Room[], Point> toreturn = super.generate(floor, tiles);
		this.xml = null;
		return toreturn;
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
		List<Element> rooms = this.xml.getChild("rooms").getChildren("room");
		this.rooms = new Room[rooms.size()];
		for (int i = 0; i < this.rooms.length; ++i)
			this.rooms[i] = new Room(this.floor, rooms.get(i));
	}

	@Override
	protected void generateTiles()
	{
		this.defaultTiles();
		String[] data = xml.getChildText("tiles").split(";");
		this.xStart = Floor.WALKABLE.x;
		this.yStart = Floor.WALKABLE.y;
		for (int y = 0; y < data.length; y++)
			for (int x = 0; x < data[y].length(); x++)
				this.tiles[x + xStart][y + yStart].setType(TileType.find(data[y].charAt(x)));
	}

	@Override
	protected void placeItems()
	{
		for (Element item : this.xml.getChild("items").getChildren(Item.XML_ROOT))
			this.tiles[Integer.parseInt(item.getAttributeValue("x")) + this.xStart][Integer.parseInt(item.getAttributeValue("y")) + this.yStart]
					.setItem(new ItemStack(item));
	}

	@Override
	protected void placeStairs()
	{}

	@Override
	protected Point placeTeam()
	{
		Element spawn = this.xml.getChild("spawn");
		return new Point(Integer.parseInt(spawn.getAttributeValue("x")) + Floor.WALKABLE.x, Integer.parseInt(spawn.getAttributeValue("y")) + Floor.WALKABLE.y);
	}

	@Override
	protected void placeTraps()
	{}

	@Override
	protected void placeWonderTiles()
	{}

	@Override
	protected void summonPokemon()
	{
			this.tiles[Integer.parseInt(pokemon.getAttributeValue("x")) + this.xStart][Integer.parseInt(pokemon.getAttributeValue("y")) + this.yStart]
					.setPokemon(new PokemonD(new Pokemon(pokemon)));
	}

}

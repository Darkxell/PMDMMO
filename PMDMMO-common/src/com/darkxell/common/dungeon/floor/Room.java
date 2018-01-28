package com.darkxell.common.dungeon.floor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.function.Predicate;

import org.jdom2.Element;

import com.darkxell.common.util.XMLUtils;

/** Represents a Room in a Floor. */
public class Room
{
	/** The floor of this Room. */
	public final Floor floor;
	/** True if this Room is a Monster House. */
	public final boolean isMonsterHouse;
	/** This Room's dimensions. */
	public final int width, height;
	/** This Room's location. */
	public final int x, y;

	public Room(Floor floor, Element xml)
	{
		this.floor = floor;
		this.x = Integer.parseInt(xml.getAttributeValue("x"));
		this.y = Integer.parseInt(xml.getAttributeValue("y"));
		this.width = Integer.parseInt(xml.getAttributeValue("width"));
		this.height = Integer.parseInt(xml.getAttributeValue("height"));
		this.isMonsterHouse = XMLUtils.getAttribute(xml, "mhouse", false);
	}

	public Room(Floor floor, int x, int y, int width, int height, boolean isMonsterHouse)
	{
		this.floor = floor;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isMonsterHouse = isMonsterHouse;
	}

	/** @return True if the input coordinates are inside this Room. */
	public boolean contains(int x, int y)
	{
		return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
	}

	public ArrayList<Tile> exits()
	{
		ArrayList<Tile> exits = new ArrayList<>(this.outline());
		exits.removeIf((Tile t) -> {
			return t.type() != TileType.GROUND;
		});
		return exits;
	}

	/** @return All tiles in this Room. */
	public ArrayList<Tile> listTiles()
	{
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		for (int y = this.y; y < this.y + this.height - 1; ++y)
			for (int x = this.x; x < this.x + this.width - 1; ++x)
				tiles.add(this.floor.tileAt(x, y));
		return tiles;
	}

	/** @return The X coordinate of the farthest tiles to the east. */
	public int maxX()
	{
		return this.x + this.width - 1;
	}

	/** @return The Y coordinate of the farthest tiles to the south. */
	public int maxY()
	{
		return this.y + this.height - 1;
	}

	/** @return All tiles that touch this Room. */
	public HashSet<Tile> outline()
	{
		HashSet<Tile> outline = new HashSet<>();
		for (int x = this.x - 1; x <= this.x + this.width; ++x)
		{
			outline.add(this.floor.tileAt(x, this.y - 1));
			outline.add(this.floor.tileAt(x, this.y + this.height));
		}
		for (int y = this.y - 1; y <= this.y + this.height; ++y)
		{
			outline.add(this.floor.tileAt(this.x - 1, y));
			outline.add(this.floor.tileAt(this.x + this.width, y));
		}
		return outline;
	}

	/** @return A random tile in this Room. */
	public Tile randomTile(Random random)
	{
		ArrayList<Tile> candidates = this.listTiles();
		return candidates.get(random.nextInt(candidates.size()));
	}

	/** @return A random tile in this Room matching the input Tile type. */
	public Tile randomTile(Random random, TileType type)
	{
		ArrayList<Tile> candidates = this.listTiles();
		candidates.removeIf(new Predicate<Tile>() {
			@Override
			public boolean test(Tile tile)
			{
				return tile.type() != type;
			}
		});
		return candidates.get(random.nextInt(candidates.size()));
	}
}

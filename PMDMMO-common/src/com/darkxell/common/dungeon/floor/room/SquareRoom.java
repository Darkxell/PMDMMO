package com.darkxell.common.dungeon.floor.room;

import java.util.ArrayList;
import java.util.HashSet;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;

/** Represents a Room in a Floor. */
public class SquareRoom extends Room
{
	/** This Room's dimensions. */
	public final int width, height;
	/** This Room's location. */
	public final int x, y;

	public SquareRoom(Floor floor, Element xml)
	{
		super(floor, xml);
		this.x = Integer.parseInt(xml.getAttributeValue("x"));
		this.y = Integer.parseInt(xml.getAttributeValue("y"));
		this.width = Integer.parseInt(xml.getAttributeValue("width"));
		this.height = Integer.parseInt(xml.getAttributeValue("height"));
	}

	public SquareRoom(Floor floor, int x, int y, int width, int height, boolean isMonsterHouse)
	{
		super(floor, isMonsterHouse);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/** @return True if the input coordinates are inside this Room. */
	public boolean contains(int x, int y)
	{
		return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
	}

	/** @return All tiles in this Room. */
	public ArrayList<Tile> listTiles()
	{
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		for (int y = this.y; y < this.y + this.height; ++y)
			for (int x = this.x; x < this.x + this.width; ++x)
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
}

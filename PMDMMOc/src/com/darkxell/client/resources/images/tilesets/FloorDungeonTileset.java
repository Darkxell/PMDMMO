package com.darkxell.client.resources.images.tilesets;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.util.Logger;

public abstract class FloorDungeonTileset extends AbstractDungeonTileset
{
	/** The last tileset that was used. Avoids reloading the same tileset at each floor. */
	private static FloorDungeonTileset previous = null;

	public static FloorDungeonTileset load(Floor floor)
	{
		int id = floor.data.terrainSpriteset();
		if (previous != null && previous.id == id && !floor.hasCustomTileset()) return previous;
		String path = "/tilesets/dungeon/dungeon-" + id + ".png";
		if (floor.hasCustomTileset()) path = "/tilesets/dungeon/static/" + floor.dungeon.id + "-" + floor.id + ".png";
		if (Res.exists(path))
		{
			if (id == -1) return previous = new CustomFloorDungeonTileset(id, path);
			return previous = new RegularFloorDungeonTileset(id, path);
		}
		Logger.w("Couldn't find tileset for floor: " + floor.dungeon.id + "-" + floor.id);
		return previous = new RegularFloorDungeonTileset(id, "/tilesets/dungeon/dungeon-0.png");
	}

	public final int id;

	public FloorDungeonTileset(int id, String path)
	{
		super(path);
		this.id = id;
	}

	public abstract BufferedImage defaultTile();

	public abstract BufferedImage tile(Tile tile);

}

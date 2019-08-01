package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.resources.image.tileset.freezone.AbstractFreezoneTileset;
import com.darkxell.common.zones.FreezoneInfo;

public class CutsceneFreezoneMap extends FreezoneMap
{

	public CutsceneFreezoneMap(String tilesetPath, FreezoneInfo info, int width, int height)
	{
		super(tilesetPath, 0, 0, info);
		this.loadLater(tilesetPath, width, height);
	}

	@Override
	protected void loadFreezoneData(String tilesetPath)
	{}

	/** Method that loads the map NOT in the super constructor because there is no way to get width and height then. */
	private void loadLater(String tilesetPath, int width, int height)
	{
		this.mapWidth = width / AbstractFreezoneTileset.TILE_SIZE;
		this.mapHeight = height / AbstractFreezoneTileset.TILE_SIZE;
		this.tiles = new FreezoneTile[this.mapWidth * this.mapHeight];
		for (int x = 0; x < this.mapWidth; ++x)
			for (int y = 0; y < this.mapHeight; ++y)
				this.tiles[x + y * this.mapWidth] = new FreezoneTile(FreezoneTile.TYPE_WALKABLE).setTileSprite(tilesetPath, x, y);
	}

}

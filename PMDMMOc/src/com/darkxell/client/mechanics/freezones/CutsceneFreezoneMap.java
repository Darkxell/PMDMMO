package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.resources.images.tilesets.AbstractTileset;
import com.darkxell.common.zones.FreezoneInfo;

public class CutsceneFreezoneMap extends FreezoneMap
{

	public CutsceneFreezoneMap(String tilesetPath, FreezoneInfo info)
	{
		super(tilesetPath, 0, 0, info);
	}

	@Override
	protected void loadFreezoneData(String tilesetPath)
	{
		AbstractTileset t = AbstractTileset.getTileset(tilesetPath);
		this.mapWidth = t.getSource().getWidth() / 8;
		this.mapHeight = t.getSource().getHeight() / 8;
		this.tiles = new FreezoneTile[this.mapWidth * this.mapHeight];
		for (int x = 0; x < this.mapWidth; ++x)
			for (int y = 0; y < this.mapHeight; ++y)
				this.tiles[x + y * this.mapWidth] = new FreezoneTile(FreezoneTile.TYPE_WALKABLE, t.SPRITES[x + y * this.mapWidth]);
	}

}

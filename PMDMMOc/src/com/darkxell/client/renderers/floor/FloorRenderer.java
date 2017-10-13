package com.darkxell.client.renderers.floor;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.resources.images.tilesets.CommonDungeonTileset;
import com.darkxell.client.resources.images.tilesets.FloorDungeonTileset;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;

public class FloorRenderer extends AbstractRenderer
{

	public final Floor floor;
	public final FloorDungeonTileset tileset;

	public FloorRenderer()
	{
		super(0, 0, MasterDungeonRenderer.LAYER_TILES);
		this.floor = Persistance.floor;
		this.tileset = FloorDungeonTileset.load(this.floor.data.terrainSpriteset());
	}

	/** Renders a Tile. */
	public void drawTile(Graphics2D g, Tile tile)
	{
		if (tile == null) return;

		BufferedImage sprite = null;
		if (tile.trapRevealed) sprite = CommonDungeonTileset.INSTANCE.trap(tile.trap.id);
		else if (tile.type() == TileType.STAIR) sprite = CommonDungeonTileset.INSTANCE.stairs(this.floor.dungeon.dungeon().direction);
		else if (tile.type() == TileType.WARP_ZONE) sprite = CommonDungeonTileset.INSTANCE.warp();
		else sprite = this.tileset.tile(tile);

		g.drawImage(sprite, tile.x * TILE_SIZE, tile.y * TILE_SIZE, null);
	}

	public void render(Graphics2D g, int width, int height)
	{
		int xStart = (int) (this.x() / TILE_SIZE), yStart = (int) (this.y() / TILE_SIZE);

		for (int x = xStart; x <= xStart + width / TILE_SIZE + 1; ++x)
			for (int y = yStart; y <= yStart + height / TILE_SIZE + 1; ++y)
				this.drawTile(g, this.floor.tileAt(x, y));
	}

}

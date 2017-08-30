package com.darkxell.client.renderers;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;

import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.resources.images.tilesets.FloorDungeonTileset;
import com.darkxell.client.resources.images.tilesets.ItemsSpriteset;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;

public class FloorRenderer
{
	private static final int ITEM_POS = (AbstractDungeonTileset.TILE_SIZE - ItemsSpriteset.ITEM_SIZE) / 2;

	public final Floor floor;
	public final FloorDungeonTileset tileset;

	public FloorRenderer(Floor floor)
	{
		this.floor = floor;
		this.tileset = new FloorDungeonTileset("resources/tilesets/dungeon-" + floor.dungeon.id + ".png");
	}

	/** Renders the Floor.
	 * 
	 * @param xPos, yPos - Translate values
	 * @param width, height - State dimension */
	public void drawFloor(Graphics2D g, int xPos, int yPos, int width, int height)
	{
		int xStart = xPos / TILE_SIZE, yStart = yPos / TILE_SIZE;
		g.translate(-xPos, -yPos);

		for (int x = xStart; x < Floor.ALL_WIDTH && x <= xStart + width / TILE_SIZE + 1; ++x)
			for (int y = yStart; y < Floor.ALL_HEIGHT && y <= yStart + height / TILE_SIZE + 1; ++y)
				this.drawTile(g, this.floor.tileAt(x, y));

		g.translate(xPos, yPos);
	}

	/** Renders a Tile. */
	public void drawTile(Graphics2D g, Tile tile)
	{
		if (tile == null) return;
		g.drawImage(this.tileset.tile(tile), tile.x * TILE_SIZE, tile.y * TILE_SIZE, null);
		if (tile.getItem() != null && tile.type() == TileType.GROUND) g.drawImage(ItemsSpriteset.instance.SPRITES[tile.getItem().item().spriteID], tile.x
				* TILE_SIZE + ITEM_POS, tile.y * TILE_SIZE + ITEM_POS, null);
	}
}

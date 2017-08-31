package com.darkxell.client.renderers;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.resources.images.tilesets.CommonDungeonTileset;
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

		for (int x = xStart; x < Floor.ALL_WIDTH && x <= xStart + width / TILE_SIZE + 1; ++x)
			for (int y = yStart; y < Floor.ALL_HEIGHT && y <= yStart + height / TILE_SIZE + 1; ++y)
				this.drawTile(g, this.floor.tileAt(x, y));

		for (int x = xStart; x < Floor.ALL_WIDTH && x <= xStart + width / TILE_SIZE + 1; ++x)
			for (int y = yStart; y < Floor.ALL_HEIGHT && y <= yStart + height / TILE_SIZE + 1; ++y)
			{
				Tile t = this.floor.tileAt(x, y);
				if (t.getPokemon() != null) PokemonRenderer.instance.draw(g, t.getPokemon(), x, y);
			}
	}

	/** Renders a Tile. */
	public void drawTile(Graphics2D g, Tile tile)
	{
		if (tile == null) return;

		BufferedImage sprite = null;
		if (tile.type() == TileType.STAIR) sprite = CommonDungeonTileset.INSTANCE.stairs(this.floor.dungeon.direction);
		else if (tile.type() == TileType.WONDER_TILE) sprite = CommonDungeonTileset.INSTANCE.wonderTile();
		else if (tile.type() == TileType.WARP_ZONE) sprite = CommonDungeonTileset.INSTANCE.warp();
		else sprite = this.tileset.tile(tile);

		g.drawImage(sprite, tile.x * TILE_SIZE, tile.y * TILE_SIZE, null);
		if (tile.getItem() != null && tile.type() == TileType.GROUND) g.drawImage(ItemsSpriteset.instance.SPRITES[tile.getItem().item().spriteID], tile.x
				* TILE_SIZE + ITEM_POS, tile.y * TILE_SIZE + ITEM_POS, null);

		g.drawRect(tile.x * TILE_SIZE, tile.y * TILE_SIZE, (tile.x + 1) * TILE_SIZE, (tile.y + 1) * TILE_SIZE);
	}

}

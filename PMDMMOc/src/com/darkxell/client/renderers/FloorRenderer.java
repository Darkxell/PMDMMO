package com.darkxell.client.renderers;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.resources.images.tilesets.CommonDungeonTileset;
import com.darkxell.client.resources.images.tilesets.FloorDungeonTileset;
import com.darkxell.client.resources.images.tilesets.ItemsSpriteset;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Logger;

public class FloorRenderer {
	private static final int ITEM_POS = (AbstractDungeonTileset.TILE_SIZE - ItemsSpriteset.ITEM_SIZE) / 2;

	public final Floor floor;
	public final FloorDungeonTileset tileset;

	public FloorRenderer(Floor floor) {
		this.floor = floor;
		this.tileset = new FloorDungeonTileset("resources/tilesets/dungeon-" + floor.dungeon.id + ".png");
	}

	/** Draws entities. */
	public void drawEntities(Graphics2D g, int xPos, int yPos, int width, int height) {
		int xStart = xPos / TILE_SIZE, yStart = yPos / TILE_SIZE;

		for (int x = xStart; x <= xStart + width / TILE_SIZE + 1; ++x)
			for (int y = yStart; y <= yStart + height / TILE_SIZE + 1; ++y) {
				Tile tile = this.floor.tileAt(x, y);
				if (tile != null) {
					if (tile.getItem() != null && tile.type() == TileType.GROUND)
						g.drawImage(ItemsSpriteset.instance.SPRITES[tile.getItem().item().getSpriteID()],
								tile.x * TILE_SIZE + ITEM_POS, tile.y * TILE_SIZE + ITEM_POS, null);
					if (tile.getPokemon() != null)
						DungeonPokemonRenderer.instance.draw(g, tile.getPokemon(), x, y);
				}
			}
	}

	/**
	 * Renders the Floor.
	 * 
	 * @param xPos,
	 *            yPos - Translate values
	 * @param width,
	 *            height - State dimension
	 */
	public void drawFloor(Graphics2D g, int xPos, int yPos, int width, int height) {
		int xStart = xPos / TILE_SIZE, yStart = yPos / TILE_SIZE;

		for (int x = xStart; x <= xStart + width / TILE_SIZE + 1; ++x)
			for (int y = yStart; y <= yStart + height / TILE_SIZE + 1; ++y)
				this.drawTile(g, this.floor.tileAt(x, y));
	}

	/** Draws the grid. */
	public void drawGrid(Graphics2D g, DungeonPokemon player, int xPos, int yPos, int width, int height) {
		int xStart = xPos / TILE_SIZE, yStart = yPos / TILE_SIZE;

		HashSet<Tile> faced = new HashSet<Tile>();
		Tile tile = player.tile;
		do {
			faced.add(tile);
			tile = tile.adjacentTile(player.facing());
		} while (tile != null);

		for (int x = xStart; x < this.floor.getWidth() && x <= xStart + width / TILE_SIZE + 1; ++x)
			for (int y = yStart; y < this.floor.getHeight() && y <= yStart + height / TILE_SIZE + 1; ++y) {
				tile = this.floor.tileAt(x, y);
				if (tile == null) Logger.e("null tile at " + x + ", " + y);
				if (tile.type() != TileType.WALL && tile.type() != TileType.WALL_END)
					g.drawImage(CommonDungeonTileset.INSTANCE.grid(faced.contains(tile)), tile.x * TILE_SIZE,
							tile.y * TILE_SIZE, null);
			}
	}

	/** Renders a Tile. */
	public void drawTile(Graphics2D g, Tile tile) {
		if (tile == null)
			return;

		BufferedImage sprite = null;
		if (tile.hasTrap() && tile.trapRevealed)
			sprite = CommonDungeonTileset.INSTANCE.trap(tile.trap.id);
		else if (tile.type() == TileType.STAIR)
			sprite = CommonDungeonTileset.INSTANCE.stairs(this.floor.dungeon.dungeon().direction);
		else if (tile.type() == TileType.WONDER_TILE)
			sprite = CommonDungeonTileset.INSTANCE.wonderTile();
		else if (tile.type() == TileType.WARP_ZONE)
			sprite = CommonDungeonTileset.INSTANCE.warp();
		else
			sprite = this.tileset.tile(tile);

		g.drawImage(sprite, tile.x * TILE_SIZE, tile.y * TILE_SIZE, null);
	}

}

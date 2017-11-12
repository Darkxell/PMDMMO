package com.darkxell.client.renderers.floor;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
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
	private static Graphics2D gs;
	private static Area inPathsTwoTiles, inPathsOneTile;
	public static final Color SHADOW = new Color(0, 0, 0, 128), VISIBLE = new Color(0, 0, 0, 0);
	private static BufferedImage shadowBuffer;

	public final Floor floor;
	public final FloorDungeonTileset tileset;

	public FloorRenderer()
	{
		super(0, 0, MasterDungeonRenderer.LAYER_TILES);
		this.floor = Persistance.floor;
		this.tileset = FloorDungeonTileset.load(this.floor.data.terrainSpriteset());
	}

	public void render(Graphics2D g, int width, int height)
	{
		int xStart = (int) (this.x() / TILE_SIZE) - 1, yStart = (int) (this.y() / TILE_SIZE) - 1;

		if (shadowBuffer == null || shadowBuffer.getWidth() != width || shadowBuffer.getHeight() != height)
		{
			shadowBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
			gs = shadowBuffer.createGraphics();
			gs.setBackground(VISIBLE);
			gs.setColor(SHADOW);

			inPathsTwoTiles = new Area(new java.awt.Rectangle(0, 0, width, height));
			inPathsOneTile = (Area) inPathsTwoTiles.clone();
			inPathsTwoTiles
					.subtract(new Area(new Ellipse2D.Double(width / 2 - TILE_SIZE * 5 / 2, height / 2 - TILE_SIZE * 5 / 2, TILE_SIZE * 5, TILE_SIZE * 5)));
			inPathsOneTile
					.subtract(new Area(new Ellipse2D.Double(width / 2 - TILE_SIZE * 3 / 2, height / 2 - TILE_SIZE * 3 / 2, TILE_SIZE * 3, TILE_SIZE * 3)));

		}

		// +1 is not sufficient, +2 is needed to cover the hole screen. God knows why -- Actually I know ! Because I need before the first and after the last.
		for (int x = xStart; x <= xStart + width / TILE_SIZE + 2; ++x)
			for (int y = yStart; y <= yStart + height / TILE_SIZE + 2; ++y)
			{
				Tile tile = this.floor.tileAt(x, y);
				BufferedImage sprite = null;

				if (tile == null) sprite = this.tileset.defaultTile();
				else if (tile.trapRevealed) sprite = CommonDungeonTileset.INSTANCE.trap(tile.trap.id);
				else if (tile.type() == TileType.STAIR) sprite = CommonDungeonTileset.INSTANCE.stairs(this.floor.dungeon.dungeon().direction);
				else if (tile.type() == TileType.WARP_ZONE) sprite = CommonDungeonTileset.INSTANCE.warp();
				else sprite = this.tileset.tile(tile);

				g.drawImage(sprite, x * TILE_SIZE, y * TILE_SIZE, null);
			}

		gs.clearRect(0, 0, width, height);
		gs.fill(inPathsTwoTiles);
		g.drawImage(shadowBuffer, (int) this.x(), (int) this.y(), null);
	}

}

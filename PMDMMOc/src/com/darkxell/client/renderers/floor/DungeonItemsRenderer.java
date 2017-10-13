package com.darkxell.client.renderers.floor;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.DungeonRenderer;
import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.resources.images.tilesets.ItemsSpriteset;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;

public class DungeonItemsRenderer extends AbstractRenderer
{
	private static final int ITEM_POS = (AbstractDungeonTileset.TILE_SIZE - ItemsSpriteset.ITEM_SIZE) / 2;

	public final Floor floor;

	public DungeonItemsRenderer()
	{
		this.floor = Persistance.floor;
		this.setZ(DungeonRenderer.LAYER_ITEMS);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		int xStart = this.x() / TILE_SIZE, yStart = this.y() / TILE_SIZE;

		for (int x = xStart; x <= xStart + width / TILE_SIZE + 1; ++x)
			for (int y = yStart; y <= yStart + height / TILE_SIZE + 1; ++y)
			{
				Tile tile = this.floor.tileAt(x, y);
				if (tile != null)
				{
					if (tile.getItem() != null && tile.type() == TileType.GROUND) g.drawImage(ItemsSpriteset.instance.SPRITES[tile.getItem().item()
							.getSpriteID()], tile.x * TILE_SIZE + ITEM_POS, tile.y * TILE_SIZE + ITEM_POS, null);
				}
			}
	}

}

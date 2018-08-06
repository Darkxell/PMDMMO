package com.darkxell.client.renderers.floor;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.resources.images.SpriteSets;
import com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset;
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
		super(0, 0, MasterDungeonRenderer.LAYER_ITEMS);
		this.floor = Persistance.floor;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		int xStart = (int) (this.x() / TILE_SIZE), yStart = (int) (this.y() / TILE_SIZE);

		for (int x = xStart; x <= xStart + width / TILE_SIZE + 1; ++x)
			for (int y = yStart; y <= yStart + height / TILE_SIZE + 1; ++y)
			{
				Tile tile = this.floor.tileAt(x, y);
				if (tile != null)
				{
					if (tile.getItem() != null && tile.type() == TileType.GROUND)
						g.drawImage(SpriteSets.items.sprite(tile.getItem()), tile.x * TILE_SIZE + ITEM_POS, tile.y * TILE_SIZE + ITEM_POS, null);
				}
			}
	}

}

package com.darkxell.client.graphics.floor;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.util.HashSet;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.graphics.AbstractRenderer;
import com.darkxell.client.graphics.MasterDungeonRenderer;
import com.darkxell.client.resources.images.Sprites.Res_Dungeon;
import com.darkxell.client.resources.images.hud.ItemsSpriteset;
import com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.item.ItemStack;

public class DungeonItemsRenderer extends AbstractRenderer
{
	private static final int ITEM_POS = (AbstractDungeonTileset.TILE_SIZE - ItemsSpriteset.ITEM_SIZE) / 2;

	public final Floor floor;
	/** Can be used to temporarily hide items (e.g. for animations.) */
	public HashSet<ItemStack> hidden = new HashSet<>();

	public DungeonItemsRenderer()
	{
		super(0, 0, MasterDungeonRenderer.LAYER_ITEMS);
		this.floor = Persistence.floor;
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
					if (tile.getItem() != null && !this.hidden.contains(tile.getItem()) && tile.type() != TileType.WALL)
						g.drawImage(Res_Dungeon.items.sprite(tile.getItem()), tile.x * TILE_SIZE + ITEM_POS, tile.y * TILE_SIZE + ITEM_POS, null);
				}
			}
	}

}

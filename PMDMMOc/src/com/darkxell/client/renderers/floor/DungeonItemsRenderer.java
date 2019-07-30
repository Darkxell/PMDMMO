package com.darkxell.client.renderers.floor;

import static com.darkxell.client.resources.image.tileset.dungeon.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.util.HashSet;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.resources.image.Sprites.DungeonSprites;
import com.darkxell.client.resources.image.dungeon.ItemsSpriteset;
import com.darkxell.client.resources.image.tileset.dungeon.AbstractDungeonTileset;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.item.ItemStack;

public class DungeonItemsRenderer extends AbstractRenderer {
    private static final int ITEM_POS = (AbstractDungeonTileset.TILE_SIZE - ItemsSpriteset.ITEM_SPRITE_SIZE) / 2;

    public final Floor floor;
    /** Can be used to temporarily hide items (e.g. for animations.) */
    public HashSet<ItemStack> hidden = new HashSet<>();

    public DungeonItemsRenderer() {
        super(0, 0, MasterDungeonRenderer.LAYER_ITEMS);
        this.floor = Persistence.floor;
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        int xStart = (int) (this.x() / TILE_SIZE), yStart = (int) (this.y() / TILE_SIZE);

        for (int x = xStart; x <= xStart + width / TILE_SIZE + 1; ++x)
            for (int y = yStart; y <= yStart + height / TILE_SIZE + 1; ++y) {
                Tile tile = this.floor.tileAt(x, y);
                if (tile != null)
                    if (tile.hasItem() && !this.hidden.contains(tile.getItem()) && tile.type() != TileType.WALL)
                        g.drawImage(DungeonSprites.items.sprite(tile.getItem()), tile.x * TILE_SIZE + ITEM_POS,
                                tile.y * TILE_SIZE + ITEM_POS, null);
            }
    }

}

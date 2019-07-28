package com.darkxell.client.renderers.floor;

import static com.darkxell.client.resources.image.dungeon.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.resources.image.Sprites.DungeonSprites;
import com.darkxell.client.resources.images.tilesets.FloorDungeonTileset;
import com.darkxell.common.dungeon.data.Dungeon.DungeonDirection;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;

public class FloorRenderer extends AbstractRenderer {

    public final Floor floor;
    public final FloorDungeonTileset tileset;

    public FloorRenderer() {
        super(0, 0, MasterDungeonRenderer.LAYER_TILES);
        this.floor = Persistence.floor;
        this.tileset = FloorDungeonTileset.load(this.floor);
    }

    public void render(Graphics2D g, int width, int height) {
        int xStart = (int) (this.x() / TILE_SIZE) - 1, yStart = (int) (this.y() / TILE_SIZE) - 1;

        // +1 is not sufficient, +2 is needed to cover the hole screen. God knows why -- Actually I know ! Because I
        // need before the first and after the last.
        for (int x = xStart; x <= xStart + width / TILE_SIZE + 2; ++x)
            for (int y = yStart; y <= yStart + height / TILE_SIZE + 2; ++y) {
                Tile tile = this.floor.tileAt(x, y);
                BufferedImage sprite = null;

                if (tile == null)
                    sprite = this.tileset.defaultTile();
                else if (tile.trapRevealed)
                    sprite = DungeonSprites.dungeonCommon.trap(tile.trap.id);
                else if (tile.type() == TileType.STAIR)
                    sprite = DungeonSprites.dungeonCommon
                            .stairs(this.floor.dungeon.dungeon().direction == DungeonDirection.DOWN);
                else if (tile.type() == TileType.WARP_ZONE)
                    sprite = DungeonSprites.dungeonCommon.warp();
                else
                    sprite = this.tileset.tile(tile);

                g.drawImage(sprite, x * TILE_SIZE, y * TILE_SIZE, null);
            }
    }
}

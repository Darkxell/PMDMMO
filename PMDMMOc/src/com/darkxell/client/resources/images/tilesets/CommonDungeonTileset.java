package com.darkxell.client.resources.images.tilesets;

import java.awt.image.BufferedImage;

public class CommonDungeonTileset extends AbstractDungeonTileset {

    public CommonDungeonTileset() {
        super("/tilesets/dungeon/dungeon-common.png", 144, 96);
    }

    /**
     * @param  facing - true if the Pokemon is facing this Tile (red grid), false else (yellow grid).
     * @return        The grid tile.
     */
    public BufferedImage grid(boolean facing) {
        return this.tileAt(facing ? 5 : 4, 0);
    }

    public BufferedImage rescue() {
        return this.tileAt(2, 0);
    }

    public BufferedImage shop() {
        return this.tileAt(3, 0);
    }

    /**
     * @param  down - true if the stairs are going down, false if they're going up.
     * @return      The stairs tile.
     */
    public BufferedImage stairs(boolean down) {
        return this.tileAt(down ? 0 : 1, 0);
    }

    /**
     * @param  id - The Trap's ID.
     * @return    A Trap tile.
     */
    public BufferedImage trap(int id) {
        int x = id + 6, y = 0;
        while (x > 5) {
            x -= 6;
            ++y;
        }
        return this.tileAt(x, y);
    }

    public BufferedImage warp() {
        return this.tileAt(1, 1);
    }

    public BufferedImage wonderTile() {
        return this.tileAt(2, 0);
    }

}

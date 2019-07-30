package com.darkxell.client.resources.image.tileset.dungeon;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;
import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.util.Logger;

public abstract class AbstractFloorDungeonTileset extends AbstractDungeonTileset {
    /** The last tileset that was used. Avoids reloading the same tileset at each floor. */
    private static AbstractFloorDungeonTileset previous = null;

    public static String getTilesetPath(Dungeon dungeon, int floor) {
        FloorData data = dungeon.getData(floor);
        int id = data.terrainSpriteset();
        String path = "/tilesets/dungeon/dungeon-" + id + ".png";
        if (data.hasCustomTileset())
            path = "/tilesets/dungeon/static/" + dungeon.id + "-" + floor + ".png";
        if (Res.exists(path))
            return path;
        Logger.w("Couldn't find tileset for floor: " + dungeon.id + "-" + floor);
        return "/tilesets/dungeon/dungeon-0.png";
    }

    public static AbstractFloorDungeonTileset load(Floor floor) {
        int id = floor.data.terrainSpriteset();
        if (previous != null && previous.id == id && !floor.data.hasCustomTileset())
            return previous;
        String path = getTilesetPath(floor.dungeon.dungeon(), floor.id);
        if (floor.data.hasCustomTileset())
            return previous = new CustomFloorDungeonTileset(id, path);
        return previous = new DungeonTerrainTileset(id, path);
    }

    public final int id;

    public AbstractFloorDungeonTileset(int id, String path) {
        super(path, 432, 576);
        this.id = id;
    }

    public abstract BufferedImage defaultTile();

    public abstract BufferedImage tile(Tile tile);

}

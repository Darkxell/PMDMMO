package com.darkxell.client.mechanics.freezones;

import java.util.ArrayList;

import com.darkxell.client.model.freezone.FreezoneModel;
import com.darkxell.client.model.freezone.FreezoneTileModel;
import com.darkxell.common.zones.FreezoneInfo;

public class CutsceneFreezoneMap extends FreezoneMap {

    public static FreezoneModel buildModel(String tilesetpath, int width, int height) {
        return new FreezoneModel(width, height, tilesetpath, new ArrayList<>());
    }

    public CutsceneFreezoneMap(FreezoneModel model, FreezoneInfo info) {
        super(model, 0, 0, info);
    }

    @Override
    protected void loadTiles(FreezoneModel model) {
        this.tiles = new FreezoneTile[this.getWidth()][this.getHeight()];
        for (int x = 0; x < this.getWidth(); ++x)
            for (int y = 0; y < this.getHeight(); ++y)
                this.tiles[x][y] = new FreezoneTile(
                        new FreezoneTileModel(x, y, x, y, false, model.getDefaultTileset()));
    }

}

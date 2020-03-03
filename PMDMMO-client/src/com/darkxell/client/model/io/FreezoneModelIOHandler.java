package com.darkxell.client.model.io;

import com.darkxell.client.model.freezone.FreezoneModel;
import com.darkxell.client.model.freezone.FreezoneTileModel;
import com.darkxell.common.model.io.ModelIOHandler;

public class FreezoneModelIOHandler extends ModelIOHandler<FreezoneModel> {

    public FreezoneModelIOHandler() {
        super(FreezoneModel.class);
    }

    @Override
    protected FreezoneModel handleAfterImport(FreezoneModel object) {

        for (FreezoneTileModel tile : object.getTiles())
            this.handleTileAfterImport(object, tile);

        return super.handleAfterImport(object);
    }

    @Override
    protected FreezoneModel handleBeforeExport(FreezoneModel object) {

        object = object.copy();
        for (FreezoneTileModel tile : object.getTiles())
            this.handleTileBeforeExport(object, tile);

        return super.handleBeforeExport(object);
    }

    private void handleTileAfterImport(FreezoneModel freezone, FreezoneTileModel tile) {
        if (tile.getSolid() == null)
            tile.setSolid(false);
        if (tile.getTileset() == null)
            tile.setTileset(freezone.getDefaultTileset());
    }

    private void handleTileBeforeExport(FreezoneModel freezone, FreezoneTileModel tile) {
        if (tile.getSolid().equals(false))
            tile.setSolid(null);
        if (tile.getTileset().equals(freezone.getDefaultTileset()))
            tile.setTileset(null);
    }

}

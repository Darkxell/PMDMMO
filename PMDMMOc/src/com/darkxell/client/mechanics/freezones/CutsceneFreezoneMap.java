package com.darkxell.client.mechanics.freezones;

import com.darkxell.common.zones.FreezoneInfo;

public class CutsceneFreezoneMap extends FreezoneMap {
    public CutsceneFreezoneMap(String tilesetPath, FreezoneInfo info) {
        super(tilesetPath, 0, 0, info);

        this.clearWalkable();
    }

    /**
     * Clear all tiles in the current domain as walkable for a cutscene.
     */
    private void clearWalkable() {
        for (int i = 0; i < this.terrain.size(); i++) {
            this.terrain.get(i).type = FreezoneTile.TYPE_WALKABLE;
        }
    }
}

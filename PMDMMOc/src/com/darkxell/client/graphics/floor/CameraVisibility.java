package com.darkxell.client.graphics.floor;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.common.ai.visibility.Visibility;
import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.pokemon.DungeonPokemon;

/**
 * Wrapper class around the current camera's Visibility function. This is to easily handle null cases but may also have
 * different behaviors, because the camera may show things the Pokemon may not be able to see.
 */
public class CameraVisibility {

    public final Floor floor;
    public final DungeonState state;

    public CameraVisibility(DungeonState state) {
        this.floor = Persistence.floor;
        this.state = state;
    }

    public Visibility camera() {
        DungeonPokemon c = this.state.getCameraPokemon();
        return c == null ? null : Persistence.floor.aiManager.getAI(c).visibility;
    }

    public boolean hasSeenTile(Tile tile) {
        if (this.camera() == null)
            return false;
        return this.camera().hasSeenTile(tile);
    }

    public boolean isItemTileVisible(Tile tile) {
        if (this.camera() == null)
            return false;
        return this.camera().isItemTileVisible(tile);
    }

    public boolean isMapVisible(DungeonPokemon pokemon) {
        if (this.camera() == null)
            return false;
        return pokemon.player() == Persistence.player || this.camera().isVisible(pokemon);
    }

    public boolean isVisible(DungeonPokemon pokemon) {
        if (Persistence.floor.data.shadows() == FloorData.NO_SHADOW)
            return true;
        return this.camera() != null && this.camera().isVisible(pokemon);
    }

}

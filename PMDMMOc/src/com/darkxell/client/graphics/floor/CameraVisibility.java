package com.darkxell.client.graphics.floor;

import java.util.HashSet;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.common.ai.visibility.Visibility;
import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.item.ItemEffects;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.DungeonPokemon;

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

    public boolean isItemVisible(Tile tile) {
        if (this.isXrayOn())
            return true;
        return this.camera().isItemVisible(tile);
    }

    public boolean isMapVisible(DungeonPokemon pokemon) {
        return pokemon.player() == Persistence.player || this.camera().isVisible(pokemon);
    }

    public boolean isVisible(DungeonPokemon pokemon) {
        if (Persistence.floor.data.shadows() == FloorData.NO_SHADOW)
            return true;
        return this.camera().isVisible(pokemon);
    }

    public boolean hasSeenTile(Tile tile) {
        return this.camera().hasSeenTile(tile);
    }

    public boolean isXrayOn() {
        ItemStack item = Persistence.player.getDungeonLeader().getItem();
        return item != null && item.item().effect() == ItemEffects.XRaySpecs;
    }

    public HashSet<Tile> visibleTiles() {
        return this.camera().currentlyVisibleTiles();
    }

}

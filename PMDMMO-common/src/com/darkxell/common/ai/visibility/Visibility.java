package com.darkxell.common.ai.visibility;

import java.util.HashSet;

import com.darkxell.common.ai.AI;
import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.item.ItemEffects;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;

public class Visibility {

    /** Reference to parent AI. */
    public final AI ai;
    /** The Tiles that this Pokemon can currently see. */
    private HashSet<Tile> currentlyVisible = new HashSet<>();
    /** The Tiles that were seen with an Item on it. */
    private HashSet<Tile> itemTiles = new HashSet<>(); // TODO has to update when Items are moved
    /** The Tiles that were already seen. */
    private HashSet<Tile> seenTiles = new HashSet<>();

    public Visibility(AI ai) {
        this.ai = ai;
    }

    /** @return The Tiles this Pokemon can currently see from the Tile it's at. */
    public HashSet<Tile> currentlyVisibleTiles() {
        return new HashSet<>(this.currentlyVisible);
    }

    /** @return <code>true</code> if the input Tile has been seen in this Floor. */
    public boolean hasSeenTile(Tile tile) {
        return this.seenTiles.contains(tile);
    }

    /**
     * @return <code>true</code> if the Item on the input Tile is visible by this Pokemon (doesn't check if there
     * actually is an Item).
     */
    public boolean isItemVisible(Tile tile) {
        if (this.isXrayOn())
            return true;
        return this.itemTiles.contains(tile);
    }

    /** @return <code>true</code> if the Pokemon is able to see the input target. */
    public boolean isVisible(DungeonPokemon target) {
        if (target.isFainted())
            return false;

        if (this.isXrayOn())
            return true;

        if (this.ai.pokemon.tile().isInRoom()) {
            if (target.tile().isInRoom())
                return this.ai.floor.roomAt(this.ai.pokemon.tile()) == this.ai.floor.roomAt(target.tile());
            return this.ai.floor.roomAt(this.ai.pokemon.tile()).outline().contains(target.tile());
        }

        return this.ai.pokemon.tile().distance(target.tile()) <= this.ai.floor.data.visionDistance();
    }

    /** @return <code>true</code> if the input Tile is currently visible by this Pokemon. */
    public boolean isVisible(Tile tile) {
        return this.currentlyVisible.contains(tile);
    }

    @Deprecated
    private boolean isXrayOn() { // TODO replace with better method
        return this.ai.pokemon.getItem() != null && this.ai.pokemon.getItem().item().effect() == ItemEffects.XRaySpecs;
    }

    /** Called when the Pokemon moves. Updates the Tiles it can see. */
    public void onPokemonMoved() {
        this.currentlyVisible.clear();

        Tile t = this.ai.pokemon.tile();
        Room r = this.ai.floor.roomAt(t.x, t.y);
        if (r == null) {
            this.visit(t);
            for (Direction direction : Direction.DIRECTIONS)
                this.visit(t.adjacentTile(direction));

            if (this.ai.floor.dungeon.dungeon().getData(this.ai.floor.id).shadows() != FloorData.DENSE_SHADOW)
                for (Tile corner : new Tile[] { t.adjacentTile(Direction.NORTHWEST),
                        t.adjacentTile(Direction.SOUTHWEST), t.adjacentTile(Direction.SOUTHEAST),
                        t.adjacentTile(Direction.NORTHEAST) })
                    for (Direction direction : Direction.DIRECTIONS)
                        this.visit(corner.adjacentTile(direction));

        } else {
            for (Tile tile : r.listTiles())
                this.visit(tile);
            for (Tile tile : r.outline())
                this.visit(tile);
        }
    }

    private void visit(Tile tile) {
        if (tile != null) {
            this.currentlyVisible.add(tile);
            this.seenTiles.add(tile);
            if (tile.getItem() != null)
                this.itemTiles.add(tile);
            else
                this.itemTiles.remove(tile);
        }
    }

}

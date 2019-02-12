package com.darkxell.common.ai.visibility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import com.darkxell.common.ai.AI;
import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.util.Direction;

public class Visibility {

    public enum VisibleObjectType {
        ITEM,
        POKEMON,
        STAIRS,
        TILE;
    }

    /** Reference to parent AI. */
    public final AI ai;
    /** The Tiles that this Pokemon can currently see. */
    private HashSet<Tile> currentlyVisible = new HashSet<>();
    /** The Tiles that were seen with an Item on it. */
    private HashSet<Tile> itemTiles = new HashSet<>();
    /** The Tiles that were already seen. */
    private HashSet<Tile> seenTiles = new HashSet<>();

    public Visibility(AI ai) {
        this.ai = ai;
    }

    /** @return The Tiles this Pokemon can currently see from the Tile it's at. */
    public HashSet<Tile> currentlyVisibleTiles() {
        if (this.hasSuperVision(VisibleObjectType.TILE))
            return new HashSet<>(this.ai.floor.listTiles());
        return new HashSet<>(this.currentlyVisible);
    }

    /** Has the input Tile been seen in this Floor? */
    public boolean hasSeenTile(Tile tile) {
        return this.seenTiles.contains(tile);
    }

    /** Can this Pokemon see all objects of the input type? */
    public boolean hasSuperVision(VisibleObjectType object) {
        if (this.ai.pokemon.ability().hasSuperVision(this.ai.floor, this.ai.pokemon, object))
            return true;
        if (this.ai.pokemon.hasItem()
                && this.ai.pokemon.getItem().item().effect().hasSuperVision(this.ai.floor, this.ai.pokemon, object))
            return true;
        for (AppliedStatusCondition condition : this.ai.pokemon.activeStatusConditions())
            if (condition.condition.hasSuperVision(this.ai.floor, this.ai.pokemon, object))
                return true;
        return false;
    }

    /** Is the Item on the input Tile is visible by this Pokemon? (doesn't check if there actually is an Item). */
    public boolean isItemTileVisible(Tile tile) {
        if (this.hasSuperVision(VisibleObjectType.ITEM))
            return true;
        return this.itemTiles.contains(tile);
    }

    /** Is the Pokemon able to see the input target? */
    public boolean isVisible(DungeonPokemon target) {
        if (target.isFainted())
            return false;

        if (this.hasSuperVision(VisibleObjectType.POKEMON))
            return true;

        if (this.ai.pokemon.tile().isInRoom()) {
            if (target.tile().isInRoom())
                return this.ai.floor.roomAt(this.ai.pokemon.tile()) == this.ai.floor.roomAt(target.tile());
            return this.ai.floor.roomAt(this.ai.pokemon.tile()).outline().contains(target.tile());
        }

        return this.ai.pokemon.tile().distance(target.tile()) <= this.ai.floor.data.visionDistance();
    }

    /** Is the input Tile currently visible by this Pokemon? */
    public boolean isVisible(Tile tile) {
        if (this.hasSuperVision(VisibleObjectType.TILE))
            return true;
        if (this.hasSuperVision(VisibleObjectType.STAIRS) && tile.type() == TileType.STAIR)
            return true;
        return this.currentlyVisible.contains(tile);
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

    /** @return The list of enemy Pokemon this Pokemon can see, sorted by distance to the Pokemon. */
    public ArrayList<DungeonPokemon> visibleEnemies() {
        ArrayList<DungeonPokemon> visible = new ArrayList<>();
        ArrayList<Tile> tiles = new ArrayList<>(this.currentlyVisibleTiles());
        tiles.sort(Comparator.comparingDouble(t -> ai.pokemon.tile().distance(t)));

        for (Tile t : tiles)
            if (t.getPokemon() != null && !this.ai.pokemon.isAlliedWith(t.getPokemon()))
                visible.add(t.getPokemon());

        return visible;
    }

    private void visit(Tile tile) {
        if (tile != null) {
            this.currentlyVisible.add(tile);
            this.seenTiles.add(tile);
            if (tile.hasItem())
                this.itemTiles.add(tile);
            else
                this.itemTiles.remove(tile);
        }
    }

}

package com.darkxell.common.ai.states;

import java.util.ArrayList;
import java.util.Comparator;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource.BaseEventSource;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.RandomUtil;

/** State in which the Pokemon explores the Floor. */
public class AIStateExplore extends AIState {

    /** Current Tile to reach. */
    private Tile currentDestination;

    public AIStateExplore(AI ai) {
        this(ai, null);
    }

    public AIStateExplore(AI ai, Tile startingDestination) {
        super(ai);
        this.currentDestination = startingDestination;
    }

    /** Finds the new destination to go to. */
    private void findNewDestination() {
        Direction facing = this.ai.pokemon.facing();
        ArrayList<Tile> candidates; // Finding candidate Tiles

        // If in Room, candidates are exits
        if (this.ai.pokemon.tile().isInRoom()) candidates = this.ai.floor.roomAt(this.ai.pokemon.tile()).exits();
        else {
            // Else candidates are furthest visible Tiles
            candidates = AIUtils.adjacentReachableTiles(this.ai.floor, this.ai.pokemon);
            if (candidates.size() == 0) {
                this.currentDestination = this.ai.pokemon.tile();
                return;
            }
            if (candidates.size() == 1) {
                this.currentDestination = candidates.get(0);
                return;
            }
            if (candidates.size() > 2) candidates = AIUtils.furthestWalkableTiles(this.ai.floor, this.ai.pokemon);
        }

        if (candidates.size() == 0) Logger.e(this.ai.pokemon + " didn't find a way to go :(");

        boolean continu = candidates.size() > 1;
        while (continu) // If more than one solution, we should remove the one it's coming from.
        {
            Tile delete = null;
            for (Tile t : candidates)
                if (t == this.ai.pokemon.tile()
                        || AIUtils.generalDirection(this.ai.pokemon.tile(), t) == facing.opposite()) {
                    delete = t;
                    break;
                }
            if (delete == null) continu = false;
            else {
                candidates.remove(delete);
                continu = candidates.size() > 1;
            }
        }
        candidates.sort(Comparator.naturalOrder());// Even if random, needs to sort to keep consistency

        this.currentDestination = RandomUtil.random(candidates, this.ai.floor.random);
    }

    @Override
    public Direction mayRotate() {
        if (this.ai.pokemon.tile() == this.currentDestination || this.currentDestination == null) return null;
        return AIUtils.generalDirection(this.ai.pokemon.tile(), this.currentDestination);
    }

    @Override
    public Event takeAction() {
        // If destination reached or not set, find a new one.
        if (this.ai.pokemon.tile() == this.currentDestination || this.currentDestination == null)
            this.findNewDestination();

        // Try to move towards the destination.
        Direction dir = AIUtils.direction(this.ai.pokemon, this.currentDestination);
        if (dir == null || !this.ai.pokemon.canMove(this.ai.floor))
            return new TurnSkippedEvent(this.ai.floor, BaseEventSource.PLAYER_ACTION, this.ai.pokemon);
        return new PokemonTravelEvent(this.ai.floor, BaseEventSource.PLAYER_ACTION, this.ai.pokemon, dir);
    }

    @Override
    public String toString() {
        return "Explores to " + this.currentDestination;
    }

}

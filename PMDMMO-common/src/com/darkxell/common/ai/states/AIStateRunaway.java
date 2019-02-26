package com.darkxell.common.ai.states;

import java.util.ArrayList;
import java.util.Comparator;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.util.Direction;

/** State in which the Pokemon skips turns. */
public class AIStateRunaway extends AIState {

    private static int score(Tile tile) {
        int score = 0;
        for (Direction d : Direction.DIRECTIONS)
            if (tile.adjacentTile(d).getPokemon() != null) ++score;
        return score;
    }

    public AIStateRunaway(AI ai) {
        super(ai);
    }

    @Override
    public Event takeAction() {
        Comparator<Tile> sorter = Comparator.comparingInt(AIStateRunaway::score);
        ArrayList<Tile> candidates = AIUtils.adjacentReachableTiles(this.ai.floor, this.ai.pokemon);
        if (candidates.size() == 0)
            return new TurnSkippedEvent(this.ai.floor, DungeonEventSource.PLAYER_ACTION, this.ai.pokemon);
        candidates.sort(sorter);
        return new PokemonTravelEvent(this.ai.floor, DungeonEventSource.PLAYER_ACTION, this.ai.pokemon,
                AIUtils.direction(this.ai.pokemon, candidates.get(0)));
    }

    @Override
    public String toString() {
        return "Flees";
    }

}

package com.darkxell.common.ai.states;

import java.util.ArrayList;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.RandomUtil;

/** State in which the Pokemon goes on a random Direction each turn. */
public class AIStateWanderAroundInRoom extends AIState {

    public AIStateWanderAroundInRoom(AI ai) {
        super(ai);
    }

    @Override
    public Direction mayRotate() {
        return null;
    }

    @Override
    public DungeonEvent takeAction() {
        ArrayList<Direction> directions = new ArrayList<>(Direction.DIRECTIONS);
        while (!directions.isEmpty()) {
            Direction d = RandomUtil.random(directions, this.ai.floor.random);
            directions.remove(d);
            if (this.ai.pokemon.tile().adjacentTile(d).isInRoom()
                    && this.ai.pokemon.tile().adjacentTile(d).canMoveTo(this.ai.pokemon, d, false))
                return new PokemonTravelEvent(this.ai.floor, eventSource, this.ai.pokemon, d);
        }
        return new TurnSkippedEvent(this.ai.floor, eventSource, this.ai.pokemon);
    }

    @Override
    public String toString() {
        return "Wanders around";
    }

}

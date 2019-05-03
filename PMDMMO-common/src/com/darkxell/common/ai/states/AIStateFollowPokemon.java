package com.darkxell.common.ai.states;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource.BaseEventSource;
import com.darkxell.common.event.action.PokemonRotateEvent;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.Direction;

/** State in which the Pokemon follows a Pokemon. */
public class AIStateFollowPokemon extends AIState {

    /**
     * True if the Pokemon is allowed to move. Else, this AI can is just 'RotateTowardsPokemon'.
     */
    public boolean canMove = true;
    /** The Tile the target was last seen at. */
    protected Tile lastSeen;
    /** The Pokemon to follow. */
    public final DungeonPokemon target;

    public AIStateFollowPokemon(AI ai, DungeonPokemon target) {
        super(ai);
        this.target = target;
    }

    public Tile lastSeen() {
        return this.lastSeen;
    }

    @Override
    public Direction mayRotate() {
        if (this.target.isFainted() || !this.ai.pokemon.canMove(this.ai.floor))
            return null;
        return AIUtils.generalDirection(this.ai.pokemon, this.target);
    }

    @Override
    public Event takeAction() {
        // If can see the target, update the last seen position.
        if (this.ai.visibility.isVisible(this.target))
            this.lastSeen = this.target.tile();

        Direction direction = AIUtils.generalDirection(this.ai.pokemon.tile(), this.lastSeen);
        Direction go = AIUtils.direction(this.ai.pokemon, this.lastSeen); // Direction to follow if can go closer to the
                                                                          // target
        // If can go closer to target, do so
        if (go != null && this.ai.pokemon.canMove(this.ai.floor) && this.canMove)
            return new PokemonTravelEvent(this.ai.floor, BaseEventSource.PLAYER_ACTION, this.ai.pokemon, go);

        // Else fight enemies
        Direction d = AIUtils.adjacentEnemyDirection(this.ai.floor, this.ai.pokemon);
        if (d != null && this.ai.pokemon.canAttack(this.ai.floor)) {
            LearnedMove move = AIUtils.chooseMove(this.ai);
            return new MoveSelectionEvent(this.ai.floor, BaseEventSource.PLAYER_ACTION, move, this.ai.pokemon, d);
        }

        // If no enemies, rotate towards target
        if (direction != this.ai.pokemon.facing())
            return new PokemonRotateEvent(this.ai.floor, BaseEventSource.PLAYER_ACTION, this.ai.pokemon, direction);
        return new TurnSkippedEvent(this.ai.floor, BaseEventSource.PLAYER_ACTION, this.ai.pokemon);
    }

    @Override
    public String toString() {
        return "Follows " + this.target;
    }

}

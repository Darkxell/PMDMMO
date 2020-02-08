package com.darkxell.common.move;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.behavior.MoveBehavior;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;

/** Utility class transporting all context variables relating to move handling. */
public class MoveContext {

    public final MoveUseEvent event;
    public final Floor floor;
    public final LearnedMove learnedMove;
    public final Move move;
    public final MoveBehavior moveBehavior;
    public final DungeonPokemon target;
    public final DungeonPokemon user;

    public MoveContext(Floor floor, Move move, MoveBehavior moveBehavior, DungeonPokemon user, DungeonPokemon target,
            MoveUseEvent event, LearnedMove learnedMove) {
        this.floor = floor;
        this.move = move;
        this.moveBehavior = moveBehavior;
        this.user = user;
        this.target = target;
        this.event = event;
        this.learnedMove = learnedMove;
    }

}

package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.dungeon.TrapDestroyedEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;

public class DestroyTrapEffect extends MoveEffect {

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        if (!missed && createAdditionals) {
            Tile t = moveEvent.usedMove.user.tile().adjacentTile(moveEvent.usedMove.user.facing());
            if (t != null && t.trap != null)
                effects.add(new TrapDestroyedEvent(moveEvent.floor, moveEvent, t));
        }
    }

    @Override
    public boolean hasEffectWithoutTarget(Move move, DungeonPokemon user) {
        Tile t = user.tile().adjacentTile(user.facing());
        return t.trap != null;
    }

}

package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.dungeon.TrapDestroyedEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;

public class DestroyTrapEffect extends MoveEffect {

    public DestroyTrapEffect(int id) {
        super(id);
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        if (!missed) {
            Tile t = moveEvent.usedMove.user.tile().adjacentTile(moveEvent.usedMove.user.facing());
            if (t != null && t.trap != null) effects.createEffect(new TrapDestroyedEvent(moveEvent.floor, moveEvent, t),
                    true);
        }
    }

    @Override
    public boolean allowsNoTarget(Move move, DungeonPokemon user) {
        Tile t = user.tile().adjacentTile(user.facing());
        return t.trap != null;
    }

}

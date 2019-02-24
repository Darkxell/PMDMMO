package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.BlowbackPokemonEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;

public class BlowbackEffect extends MoveEffect {

    public BlowbackEffect(int id) {
        super(id);
    }

    @Override
    public void additionalEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor,
            MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.additionalEffects(usedMove, target, flags, floor, calculator, missed, effects);

        effects.createEffect(
                new BlowbackPokemonEvent(floor, eventSource,
                        target, this.direction(usedMove, target, flags, floor, calculator, missed, effects)),
                usedMove, target, floor, missed, false, target);
    }

    protected Direction direction(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor,
            MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        return usedMove.direction;
    }

}

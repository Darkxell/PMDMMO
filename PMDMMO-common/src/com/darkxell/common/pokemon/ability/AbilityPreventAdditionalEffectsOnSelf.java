package com.darkxell.common.pokemon.ability;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AbilityPreventAdditionalEffectsOnSelf extends AbilityModifyMoveEffect {

    public AbilityPreventAdditionalEffectsOnSelf(int id) {
        super(id);
    }

    @Override
    public DungeonEvent modify(DungeonEvent effect, MoveUseEvent moveEvent, boolean missed, boolean isAdditional,
            boolean amIUser, DungeonPokemon directedAt) {
        if (isAdditional && !amIUser && target == directedAt)
            return null; // If this changes, check AbilityAbsorbDamage
        return effect;
    }

}

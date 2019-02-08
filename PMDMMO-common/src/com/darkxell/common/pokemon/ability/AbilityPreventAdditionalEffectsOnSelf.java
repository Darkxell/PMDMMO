package com.darkxell.common.pokemon.ability;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AbilityPreventAdditionalEffectsOnSelf extends AbilityModifyMoveEffect {

    public AbilityPreventAdditionalEffectsOnSelf(int id) {
        super(id);
    }

    @Override
    public DungeonEvent modify(DungeonEvent effect, MoveUse usedMove, DungeonPokemon target, Floor floor,
            boolean missed, boolean isAdditional, boolean amIUser, DungeonPokemon directedAt) {
        if (isAdditional && !amIUser && target == directedAt)
            return null; // If this changes, check AbilityAbsorbDamage
        return effect;
    }

}

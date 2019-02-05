package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AbilityMultiplyIncomingDamage extends Ability {

    public final MoveCategory category;
    public final double multiplier;

    public AbilityMultiplyIncomingDamage(int id, MoveCategory category, double multiplier) {
        super(id);
        this.category = category;
        this.multiplier = multiplier;
    }

    @Override
    public double damageMultiplier(MoveUse move, DungeonPokemon target, boolean isUser, Floor floor, String[] flags,
            ArrayList<DungeonEvent> events) {
        if (target.ability() == this && move.move.move().category == this.category) {
            events.add(new TriggeredAbilityEvent(floor, target));
            return this.multiplier;
        }
        return super.damageMultiplier(move, target, isUser, floor, flags, events);
    }

}

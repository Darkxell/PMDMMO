package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.move.Move.MoveCategory;

public class AbilityMultiplyIncomingDamage extends Ability {

    public final MoveCategory category;
    public final double multiplier;

    public AbilityMultiplyIncomingDamage(int id, MoveCategory category, double multiplier) {
        super(id);
        this.category = category;
        this.multiplier = multiplier;
    }

    @Override
    public double damageMultiplier(boolean isUser, MoveUseEvent moveEvent, ArrayList<DungeonEvent> events) {
        if (moveEvent.target.ability() == this && moveEvent.usedMove.move.move().category == this.category) {
            events.add(new TriggeredAbilityEvent(moveEvent.floor, moveEvent, moveEvent.target));
            return this.multiplier;
        }
        return super.damageMultiplier(isUser, moveEvent, events);
    }

}

package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;

public class AbilityPreventOneShot extends Ability {

    public AbilityPreventOneShot(int id) {
        super(id);
    }

    @Override
    public double applyDamageModifications(double damage, boolean isUser, MoveUseEvent moveEvent, ArrayList<DungeonEvent> events) {
        if (moveEvent.target.ability() == this && moveEvent.target.getHp() >= moveEvent.target.getMaxHP() && damage >= moveEvent.target.getHp()) {
            events.add(new TriggeredAbilityEvent(moveEvent.floor, moveEvent, moveEvent.target));
            return moveEvent.target.getHp() - 1;
        }
        return super.applyDamageModifications(damage, isUser, moveEvent, events);
    }

}

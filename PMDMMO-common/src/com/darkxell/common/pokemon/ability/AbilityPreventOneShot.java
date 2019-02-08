package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AbilityPreventOneShot extends Ability {

    public AbilityPreventOneShot(int id) {
        super(id);
    }

    @Override
    public double applyDamageModifications(double damage, MoveUse move, DungeonPokemon target, boolean isUser,
            Floor floor, ArrayList<DungeonEvent> events) {
        if (target.ability() == this && target.getHp() >= target.getMaxHP() && damage >= target.getHp()) {
            events.add(new TriggeredAbilityEvent(floor, target));
            return target.getHp() - 1;
        }
        return super.applyDamageModifications(damage, move, target, isUser, floor, events);
    }

}

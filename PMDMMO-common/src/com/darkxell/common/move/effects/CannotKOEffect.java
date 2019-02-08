package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.pokemon.DungeonPokemon;

public class CannotKOEffect extends MoveEffect {

    public CannotKOEffect(int id) {
        super(id);
    }

    @Override
    public double applyDamageModifications(double damage, MoveUse move, DungeonPokemon target, boolean isUser,
            Floor floor, ArrayList<DungeonEvent> events) {
        if (damage >= target.getHp())
            return target.getHp() - 1;
        return super.applyDamageModifications(damage, move, target, isUser, floor, events);
    }

}

package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;

public class DropItemEffect extends MoveEffect {

    public DropItemEffect(int id) {
        super(id);
    }

    @Override
    public void additionalEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor,
            MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.additionalEffects(usedMove, target, flags, floor, calculator, missed, effects);

        if (!missed && target.getItem() != null)
            effects.createEffect(new ItemMovedEvent(floor, ItemAction.AUTO, null, target, 0, target.tile(), 0),
                    usedMove, target, floor, missed, true, target);
    }

}

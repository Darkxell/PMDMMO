package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.dungeon.FloorStatusCreatedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.FloorStatus;
import com.darkxell.common.util.language.Message;

public class CreateFloorStatusEffect extends MoveEffect {

    public final FloorStatus status;

    public CreateFloorStatusEffect(int id, FloorStatus status) {
        super(id);
        this.status = status;
    }

    @Override
    public void additionalEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor,
            MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.additionalEffects(usedMove, target, flags, floor, calculator, missed, effects);

        if (!missed)
            effects.createEffect(new FloorStatusCreatedEvent(floor, eventSource, this.status.create(usedMove.user, floor.random)),
                    usedMove, target, floor, missed, usedMove.move.move().dealsDamage, target);
    }

    @Override
    public Message descriptionBase(Move move) {
        return new Message("move.info.floor_status").addReplacement("<status>", this.status.name());
    }

}

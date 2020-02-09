package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Message;

public class RandomAttacksEffect extends MoveEffect {

    public final int attacks;

    public RandomAttacksEffect(int attacks) {
        this.attacks = attacks;
    }

    @Override
    public boolean alterMoveCreation(MoveSelectionEvent moveEvent, ArrayList<Event> events) {
        for (int i = 0; i < this.attacks; ++i) {
            Direction d = RandomUtil.random(Direction.DIRECTIONS, moveEvent.floor.random);
            MoveUseEvent e = new MoveUseEvent(moveEvent.floor, moveEvent, moveEvent.usedMove(),
                    moveEvent.usedMove().user.tile().adjacentTile(d).getPokemon());
            e.direction = d;
            events.add(e);
        }
        return true;
    }

    @Override
    public Message description() {
        return new Message("move.info.random_attacks").addReplacement("<attacks>", String.valueOf(this.attacks));
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {}

}

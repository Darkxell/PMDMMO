package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Message;

public class RandomAttacksEffect extends MoveEffect {

    public final int attacks;

    public RandomAttacksEffect(int id, int attacks) {
        super(id);
        this.attacks = attacks;
    }

    @Override
    public void createMoves(MoveSelectionEvent moveEvent, ArrayList<DungeonEvent> events) {
        for (int i = 0; i < this.attacks; ++i) {
            Direction d = RandomUtil.random(Direction.DIRECTIONS, moveEvent.floor.random);
            MoveUseEvent e = new MoveUseEvent(moveEvent.floor, moveEvent, moveEvent.usedMove(),
                    moveEvent.usedMove().user.tile().adjacentTile(d).getPokemon());
            e.direction = d;
            events.add(e);
        }
    }

    @Override
    public Message descriptionBase(Move move) {
        return new Message("move.info.random_attacks").addReplacement("<attacks>", String.valueOf(this.attacks));
    }

}

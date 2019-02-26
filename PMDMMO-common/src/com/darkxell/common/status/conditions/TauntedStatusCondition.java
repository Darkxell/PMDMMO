package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.util.language.Message;

public class TauntedStatusCondition extends StatusCondition {

    public TauntedStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public void onPreEvent(Floor floor, Event event, DungeonPokemon concerned,
            ArrayList<Event> resultingEvents) {
        if (event instanceof MoveSelectionEvent && concerned.hasStatusCondition(this)
                && ((MoveSelectionEvent) event).usedMove().user == concerned
                && !((MoveSelectionEvent) event).usedMove().move.move().dealsDamage) {
            event.consume();
            resultingEvents
                    .add(new MessageEvent(floor, event, new Message("status.trigger.27").addReplacement("<pokemon>", concerned.getNickname())));
        }
        super.onPreEvent(floor, event, concerned, resultingEvents);
    }

    @Override
    public boolean preventsUsingMove(LearnedMove move, DungeonPokemon pokemon, Floor floor) {
        if (pokemon.hasStatusCondition(this) && move != null && !move.move().dealsDamage) return true;
        return super.preventsUsingMove(move, pokemon, floor);
    }

}

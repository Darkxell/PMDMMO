package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.util.language.Message;

public class PreventStatLossStatusCondition extends PreventStatusCondition {

    public PreventStatLossStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public void onPreEvent(Floor floor, Event event, DungeonPokemon concerned, ArrayList<Event> resultingEvents) {
        super.onPreEvent(floor, event, concerned, resultingEvents);

        if (event instanceof StatChangedEvent) {
            StatChangedEvent e = (StatChangedEvent) event;
            if (e.target == concerned && e.stage < 0) {
                if (e.eventSource instanceof MoveUseEvent) {
                    MoveUseEvent source = (MoveUseEvent) e.eventSource;
                    if (source.usedMove.user == concerned) {
                        return; // Don't prevent if it's self-inflicted
                    }
                }

                e.consume();
                resultingEvents.add(new MessageEvent(e.floor, event,
                        new Message("status.trigger." + this.id).addReplacement("<pokemon>", concerned.getNickname())
                                .addReplacement("<stat>", e.stat.getName())));
            }
        }
    }

    @Override
    public boolean prevents(StatusCondition condition) {
        return false;
    }

}

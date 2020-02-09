package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.util.language.Message;

public class RedirectAttacksStatusCondition extends StatusCondition {

    public final double redirectedMultiplier;

    public RedirectAttacksStatusCondition(int id, boolean isAilment, int durationMin, int durationMax,
            double redirectedMultiplier) {
        super(id, isAilment, durationMin, durationMax);
        this.redirectedMultiplier = redirectedMultiplier;
    }

    @Override
    public double damageMultiplier(boolean isUser, MoveContext context, ArrayList<Event> events) {
        if (isUser)
            for (String flag : context.event.flags())
                if (flag.equals("redirected"))
                    return this.redirectedMultiplier;
        return super.damageMultiplier(isUser, context, events);
    }

    @Override
    public void onPreEvent(Floor floor, Event event, DungeonPokemon concerned, ArrayList<Event> resultingEvents) {
        super.onPreEvent(floor, event, concerned, resultingEvents);

        if (event instanceof MoveUseEvent) {
            MoveUseEvent e = (MoveUseEvent) event;
            if (e.target == concerned && !e.hasFlag("redirected")) {
                event.consume();
                MoveUseEvent redirected = new MoveUseEvent(floor, e.eventSource,
                        new MoveUse(floor, e.usedMove.move, concerned, concerned.facing(), e.eventSource),
                        e.usedMove.user);
                redirected.addFlag("redirected");
                resultingEvents.add(new MessageEvent(floor, event,
                        new Message("status.trigger.43").addReplacement("<pokemon>", concerned.toString())));
                resultingEvents.add(redirected);
            }
        }
    }

}

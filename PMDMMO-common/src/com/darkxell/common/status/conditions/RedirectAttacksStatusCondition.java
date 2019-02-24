package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
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
    public double damageMultiplier(MoveUse move, DungeonPokemon target, boolean isUser, Floor floor, String[] flags,
            ArrayList<DungeonEvent> events) {
        if (isUser)
            for (String flag : flags)
                if (flag.equals("redirected"))
                    return this.redirectedMultiplier;
        return super.damageMultiplier(move, target, isUser, floor, flags, events);
    }

    @Override
    public void onPreEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned,
            ArrayList<DungeonEvent> resultingEvents) {
        super.onPreEvent(floor, event, concerned, resultingEvents);

        if (event instanceof MoveUseEvent) {
            MoveUseEvent e = (MoveUseEvent) event;
            if (e.target == concerned && !e.hasFlag("redirected")) {
                event.consume();
                MoveUseEvent redirected = new MoveUseEvent(floor,
                        new MoveUse(floor, e.usedMove.move, concerned, concerned.facing()), e.usedMove.user);
                redirected.addFlag("redirected");
                resultingEvents.add(new MessageEvent(floor,
                        eventSource, new Message("status.trigger.43").addReplacement("<pokemon>", concerned.toString())));
                resultingEvents.add(redirected);
            }
        }
    }

}

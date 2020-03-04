package com.darkxell.common.trap;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.dungeon.TrapSteppedOnEvent;
import com.darkxell.common.event.stats.StatResetEvent;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.util.language.Message;

public class WonderTileTrap extends Trap {

    public WonderTileTrap(int id) {
        super(id);
    }

    @Override
    public void onPokemonStep(TrapSteppedOnEvent trapEvent, ArrayList<Event> events) {
        events.add(new MessageEvent(trapEvent.floor, trapEvent,
                new Message("stat.reset.debuff").addReplacement("<pokemon>", trapEvent.pokemon.getNickname()),
                trapEvent.pokemon.player()));

        for (Stat s : Stat.values())
            if (s != Stat.Speed && s != Stat.Health) {
                int stage = trapEvent.pokemon.stats.getStage(s);
                if (stage < Stat.DEFAULT_STAGE)
                    events.add(new StatResetEvent(trapEvent.floor, trapEvent, trapEvent.pokemon, s));
            }

        if (trapEvent.pokemon.stats.getMoveSpeed() < 1)
            events.add(new StatResetEvent(trapEvent.floor, trapEvent, trapEvent.pokemon, Stat.Speed));
    }

}

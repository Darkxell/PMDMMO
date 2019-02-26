package com.darkxell.common.trap;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.dungeon.TrapSteppedOnEvent;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.util.language.Message;

public class WonderTileTrap extends Trap {

    public WonderTileTrap(int id) {
        super(id);
    }

    @Override
    public void onPokemonStep(TrapSteppedOnEvent trapEvent, ArrayList<Event> events) {
        for (Stat s : Stat.values())
            if (s != Stat.Speed) {
                int stage = trapEvent.pokemon.stats.getStage(s);
                if (stage < Stat.DEFAULT_STAGE) trapEvent.pokemon.stats.setStage(s, Stat.DEFAULT_STAGE);
            }

        if (trapEvent.pokemon.stats.getMoveSpeed() < 1) trapEvent.pokemon.stats.resetSpeed();

        events.add(new MessageEvent(trapEvent.floor, trapEvent,
                new Message("stat.reset").addReplacement("<pokemon>", trapEvent.pokemon.getNickname()), trapEvent.pokemon.player()));
    }

}

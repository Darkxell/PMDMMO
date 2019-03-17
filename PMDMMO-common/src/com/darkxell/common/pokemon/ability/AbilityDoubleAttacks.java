package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.weather.Weather;

public class AbilityDoubleAttacks extends Ability {

    /** Weathers that activate this Ability. */
    private Weather[] activators;

    public AbilityDoubleAttacks(int id, Weather... activators) {
        super(id);
        this.activators = activators;
    }

    @Override
    public void onPostEvent(Floor floor, Event event, DungeonPokemon concerned,
            ArrayList<Event> resultingEvents) {
        super.onPostEvent(floor, event, concerned, resultingEvents);
        if (event instanceof MoveSelectionEvent && !event.hasFlag("isdoubled")) {
            MoveSelectionEvent e = (MoveSelectionEvent) event;
            boolean active = false;
            Weather current = floor.currentWeather().weather;

            for (Weather a : this.activators)
                if (current == a) {
                    active = e.usedMove().user.ability() == this && e.usedMove().user == concerned;
                    break;
                }

            if (active) {
                Event abilityevent = new TriggeredAbilityEvent(floor, event, e.usedMove().user)
                        .setPriority(Event.PRIORITY_ACTION_END);
                resultingEvents.add(abilityevent);
                MoveSelectionEvent n = new MoveSelectionEvent(e.floor, abilityevent, e.usedMove().move,
                        e.usedMove().user, e.usedMove().direction);
                e.addFlag("isdoubled");
                n.addFlag("isdoubled");
                n.setConsumesNoPP();
                n.setPriority(Event.PRIORITY_ACTION_END);
                resultingEvents.add(n);
            }
        }
    }

}

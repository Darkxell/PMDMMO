package com.darkxell.common.event.dungeon.weather;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.weather.WeatherDamaging;

public class WeatherDamageEvent extends Event {

    public final int damage;
    private final ArrayList<DungeonPokemon> pokemon;
    public final WeatherDamaging source;

    public WeatherDamageEvent(Floor floor, DungeonEventSource eventSource, WeatherDamaging source,
            ArrayList<DungeonPokemon> pokemon, int damage) {
        super(floor, eventSource);
        this.source = source;
        this.pokemon = pokemon;
        this.damage = damage;
    }

    @Override
    public String loggerMessage() {
        return this.source.name() + " dealt " + this.damage + " damage to " + this.pokemon.size() + " Pokemon.";
    }

    @Override
    public ArrayList<Event> processServer() {
        for (DungeonPokemon pokemon : this.pokemon)
            this.resultingEvents
                    .add(new DamageDealtEvent(this.floor, this, pokemon, this.source, DamageType.WEATHER, this.damage));
        return super.processServer();
    }

}

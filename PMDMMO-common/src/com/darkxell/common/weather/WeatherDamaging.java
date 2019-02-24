package com.darkxell.common.weather;

import java.awt.Color;
import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.event.dungeon.weather.WeatherDamageEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.event.stats.ExperienceGeneratedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;

public class WeatherDamaging extends Weather implements DamageSource {
    public static final int PERIOD = 10, DAMAGE = 5;

    /** The Damage this Weather deals. */
    public final int damage;
    /** A list of types that are immune to this Weather. */
    public final PokemonType[] immunes;
    /** The number of ticks it takes for this Weather to deal damage. */
    public final int period;

    public WeatherDamaging(int id, Color layer, int period, int damage, PokemonType... immunes) {
        super(id, layer);
        this.period = period;
        this.damage = damage;
        this.immunes = immunes;
    }

    @Override
    public ExperienceGeneratedEvent getExperienceEvent() {
        return null;
    }

    @Override
    public ArrayList<DungeonEvent> weatherTick(Floor floor, int tick) {
        ArrayList<DungeonEvent> e = super.weatherTick(floor, tick);
        if (tick % this.period == 0) {
            ArrayList<DungeonPokemon> pokemon = floor.listPokemon();
            pokemon.removeIf(pokemon1 -> {
                for (PokemonType type : immunes)
                    if (pokemon1.species().isType(type))
                        return true;
                return false;
            });
            e.add(new WeatherDamageEvent(floor, DungeonEventSource.TRIGGER, this, pokemon, this.damage));
        }
        return e;
    }

}

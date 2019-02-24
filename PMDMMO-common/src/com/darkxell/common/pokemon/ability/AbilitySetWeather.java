package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.dungeon.weather.WeatherCreatedEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.weather.Weather;
import com.darkxell.common.weather.WeatherSource;

public class AbilitySetWeather extends Ability implements WeatherSource {

    public final int cycle;
    public final Weather weather;

    public AbilitySetWeather(int id, Weather weather, int cycle) {
        super(id);
        this.weather = weather;
        this.cycle = cycle;
    }

    @Override
    public boolean isOver() {
        return false;
    }

    @Override
    public void onFloorStart(Floor floor, DungeonPokemon pokemon, ArrayList<DungeonEvent> events) {
        super.onFloorStart(floor, pokemon, events);
        events.add(new TriggeredAbilityEvent(floor, pokemon) {
            @Override
            public boolean isValid() {
                return floor.currentWeather().weather != weather;
            }
        });
        events.add(new WeatherCreatedEvent(this.weather.create(floor, this, -1), eventSource));
    }

    @Override
    public void onTurnStart(Floor floor, DungeonPokemon pokemon, ArrayList<DungeonEvent> events) {
        super.onTurnStart(floor, pokemon, events);
        if (floor.turnCount() % this.cycle == 0 && floor.currentWeather().weather != this.weather) {
            events.add(new TriggeredAbilityEvent(floor, pokemon));
            events.add(new WeatherCreatedEvent(this.weather.create(floor, this, -1), eventSource));
        }
    }

}

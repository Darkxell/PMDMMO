package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.weather.Weather;

public class WeatherHealEffect extends MoveEffect {

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {
        if (!missed && !createAdditionals) {
            int health = 50;
            Weather w = moveEvent.floor.currentWeather().weather;
            if (w == Weather.SUNNY)
                health = 80;
            if (w == Weather.SANDSTORM)
                health = 30;
            if (w == Weather.CLOUDS)
                health = 40;
            if (w == Weather.RAIN || w == Weather.HAIL)
                health = 10;
            if (w == Weather.SNOW)
                health = 1;
            effects.add(new HealthRestoredEvent(moveEvent.floor, moveEvent, moveEvent.target, health));
        }
    }

}

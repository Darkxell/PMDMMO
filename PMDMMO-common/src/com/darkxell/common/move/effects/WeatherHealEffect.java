package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.weather.Weather;

public class WeatherHealEffect extends MoveEffect {

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {
        if (!missed && context.target != null && !createAdditionals) {
            int health = 50;
            Weather w = context.floor.currentWeather().weather;
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
            effects.add(new HealthRestoredEvent(context.floor, context.event, context.target, health));
        }
    }

}

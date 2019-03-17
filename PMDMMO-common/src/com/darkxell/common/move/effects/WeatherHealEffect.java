package com.darkxell.common.move.effects;

import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.weather.Weather;

public class WeatherHealEffect extends MoveEffect {

    public WeatherHealEffect(int id) {
        super(id);
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);
        if (!missed) {
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
            effects.createEffect(new HealthRestoredEvent(moveEvent.floor, moveEvent, moveEvent.target, health),
                    moveEvent, missed, false);
        }
    }

}

package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.dungeon.weather.WeatherCreatedEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.weather.ActiveWeather;
import com.darkxell.common.weather.Weather;

public class WeatherChangeEffect extends MoveEffect {

    public final Weather weather;

    public WeatherChangeEffect(Weather weather) {
        this.weather = weather;
    }

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        if (!missed && !createAdditionals) {
            ActiveWeather weather = new ActiveWeather(this.weather, moveEvent.usedMove, moveEvent.floor, 5);
            Event event = new WeatherCreatedEvent(weather, moveEvent);
            effects.add(event);
        }
    }

    @Override
    public Message description() {
        return new Message("move.info.weather").addReplacement("<weather>", this.weather.name());
    }

}

package com.darkxell.common.move.effects;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.dungeon.weather.WeatherCreatedEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.weather.ActiveWeather;
import com.darkxell.common.weather.Weather;

public class WeatherChangeEffect extends MoveEffect {

    public final Weather weather;

    public WeatherChangeEffect(int id, Weather weather) {
        super(id);
        this.weather = weather;
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        if (!missed) {
            ActiveWeather weather = new ActiveWeather(this.weather, moveEvent.usedMove, moveEvent.floor, 5);
            Event event = new WeatherCreatedEvent(weather, moveEvent);
            effects.createEffect(event, false);
        }
    }

    @Override
    public Message descriptionBase(Move move) {
        return new Message("move.info.weather").addReplacement("<weather>", this.weather.name());
    }

}

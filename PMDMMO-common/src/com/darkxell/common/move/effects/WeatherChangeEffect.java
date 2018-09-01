package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.dungeon.weather.WeatherCreatedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.weather.ActiveWeather;
import com.darkxell.common.weather.Weather;

public class WeatherChangeEffect extends MoveEffect
{

	public final Weather weather;

	public WeatherChangeEffect(int id, Weather weather)
	{
		super(id);
		this.weather = weather;
	}

	@Override
	public void additionalEffects(MoveUse usedMove, DungeonPokemon target, Floor floor, MoveEffectCalculator calculator, boolean missed, MoveEvents effects)
	{
		super.additionalEffects(usedMove, target, floor, calculator, missed, effects);

		effects.createEffect(new WeatherCreatedEvent(new ActiveWeather(this.weather, usedMove, floor, 5)), usedMove, target, floor, missed, false, null);
	}

}

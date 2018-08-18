package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.weather.Weather;

public class WeatherHealEffect extends MoveEffect
{

	public WeatherHealEffect(int id)
	{
		super(id);
	}

	@Override
	protected void moveEffects(MoveUse usedMove, DungeonPokemon target, Floor floor, MoveEffectCalculator calculator, boolean missed)
	{
		super.moveEffects(usedMove, target, floor, calculator, missed);
		if (!missed)
		{
			int health = 50;
			Weather w = floor.currentWeather().weather;
			if (w == Weather.SUNNY) health = 80;
			if (w == Weather.SANDSTORM) health = 30;
			if (w == Weather.CLOUDS) health = 40;
			if (w == Weather.RAIN || w == Weather.HAIL) health = 10;
			if (w == Weather.SNOW) health = 1;
			this.createEffect(new HealthRestoredEvent(floor, target, health), usedMove, target, floor, missed, false, target);
		}
	}

}

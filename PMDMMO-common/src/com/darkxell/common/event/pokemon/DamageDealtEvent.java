package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.stats.BellyChangedEvent;
import com.darkxell.common.event.stats.ExperienceGeneratedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.weather.WeatherDamaging;

public class DamageDealtEvent extends DungeonEvent
{

	public static interface DamageSource
	{
		/** @return The ExperienceGainedEvent to add experience to. */
		public ExperienceGeneratedEvent getExperienceEvent();
	}

	public final int damage;
	/** The Source that dealt damage. */
	public final DamageSource source;
	public final DungeonPokemon target;

	public DamageDealtEvent(Floor floor, DungeonPokemon target, DamageSource source, int damage)
	{
		super(floor);
		this.target = target;
		this.source = source;
		this.damage = damage;
	}

	@Override
	public String loggerMessage()
	{
		return this.target + " took " + this.damage + " from " + this.source;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.source instanceof WeatherDamaging) this.messages.add(
				new Message("weather.damage_dealt").addReplacement("<pokemon>", target.getNickname()).addReplacement("<amount>", Integer.toString(damage)));
		else if (!(this.source instanceof BellyChangedEvent)) this.messages
				.add(new Message("move.damage_dealt").addReplacement("<pokemon>", target.getNickname()).addReplacement("<amount>", Integer.toString(damage)));

		this.target.setHP(this.target.getHp() - this.damage);
		if (this.target.getHp() == 0) this.resultingEvents.add(new FaintedPokemonEvent(this.floor, this.target, this.source));
		return super.processServer();
	}

}

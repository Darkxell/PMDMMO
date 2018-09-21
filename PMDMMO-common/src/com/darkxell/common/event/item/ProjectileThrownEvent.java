package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.event.stats.ExperienceGeneratedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.effects.ThrowableItemEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;

public class ProjectileThrownEvent extends DungeonEvent implements DamageSource
{

	public final ExperienceGeneratedEvent experienceEvent;
	public final Item item;
	public final DungeonPokemon thrower, target;

	public ProjectileThrownEvent(Floor floor, Item item, DungeonPokemon thrower, DungeonPokemon target)
	{
		super(floor);
		this.item = item;
		this.thrower = thrower;
		this.target = target;
		this.experienceEvent = this.thrower.type == DungeonPokemonType.TEAM_MEMBER ? new ExperienceGeneratedEvent(this.floor, this.thrower.player()) : null;
	}

	@Override
	public ExperienceGeneratedEvent getExperienceEvent()
	{
		return this.experienceEvent;
	}

	@Override
	public String loggerMessage()
	{
		return this.thrower + " threw " + this.item + " (target= " + this.target + ").";
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.item.effect() instanceof ThrowableItemEffect && this.target != null)
		{
			this.resultingEvents.add(new DamageDealtEvent(this.floor, this.target, this, ((ThrowableItemEffect) this.item.effect()).damage));
			this.resultingEvents.add(this.experienceEvent);
		}
		return super.processServer();
	}

}

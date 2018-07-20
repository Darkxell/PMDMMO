package com.darkxell.common.status;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class StatusConditionInstance
{

	/** This Status Condition's ID. */
	public final StatusCondition condition;
	/** This Status Condition's duration. */
	public final int duration;
	/** The Pokémon this Status condition affects. */
	public final DungeonPokemon pokemon;
	/** The number of turns this Condition has been in effect. */
	int tick;

	public StatusConditionInstance(StatusCondition condition, DungeonPokemon pokemon, int duration)
	{
		this.condition = condition;
		this.pokemon = pokemon;
		this.duration = duration;
		this.tick = 0;
	}

	public Message endMessage()
	{
		return new Message("status.end." + this.condition.id).addReplacement("<pokemon>", this.pokemon.getNickname());
	}

	public int getTurns()
	{
		return this.tick;
	}

	public boolean isOver()
	{
		if (this.duration == -1) return false;
		return this.tick >= this.duration;
	}

	public Message startMessage()
	{
		return new Message("status.start." + this.condition.id).addReplacement("<pokemon>", this.pokemon.getNickname());
	}

	public void tick(Floor floor, ArrayList<DungeonEvent> events)
	{
		if (!this.isOver()) this.condition.tick(floor, this, events);
		++this.tick;
		if (this.isOver()) events.add(new StatusConditionEndedEvent(floor, this));
	}

}

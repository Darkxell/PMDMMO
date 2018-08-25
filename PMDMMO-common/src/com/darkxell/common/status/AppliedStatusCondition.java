package com.darkxell.common.status;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventListener;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Lang;
import com.darkxell.common.util.language.Message;

public class AppliedStatusCondition implements DungeonEventListener
{

	/** True if the Pokemon this Status condition affects has acted this turn while this Status condition was active.<br>
	 * This is necessary due to the ticking of Status conditions happening at the end of turn: because Pokemon in teams act first, they would not suffer from conditions that prevent action for a single turn if this attribute wasn't used. */
	private boolean actedWhileApplied;
	/** This Status Condition's ID. */
	public final StatusCondition condition;
	/** This Status Condition's duration. */
	public final int duration;
	/** The Pokemon this Status condition affects. */
	public final DungeonPokemon pokemon;
	public final Object source;
	/** The number of turns this Condition has been in effect. */
	int tick;

	public AppliedStatusCondition(StatusCondition condition, DungeonPokemon pokemon, Object source, int duration)
	{
		this.condition = condition;
		this.pokemon = pokemon;
		this.source = source;
		this.duration = duration;
		this.tick = 0;
	}

	/** @return {@link AppliedStatusCondition#actedWhileApplied}. */
	public boolean actedWhileApplied()
	{
		return this.actedWhileApplied;
	}

	public Message endMessage()
	{
		String id = "status.end." + this.condition.id;
		if (!Lang.containsKey(id)) return null;
		return new Message(id).addReplacement("<pokemon>", this.pokemon.getNickname());
	}

	public void finish(Floor floor, ArrayList<DungeonEvent> events)
	{
		events.add(new StatusConditionEndedEvent(floor, this));
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

	@Override
	public void onPostEvent(Floor floor, DungeonEvent event, ArrayList<DungeonEvent> resultingEvents)
	{
		if (event.actor() == this.pokemon) this.actedWhileApplied = true;
		this.condition.onPostEvent(floor, event, resultingEvents);
	}

	@Override
	public void onPreEvent(Floor floor, DungeonEvent event, ArrayList<DungeonEvent> resultingEvents)
	{
		this.condition.onPreEvent(floor, event, resultingEvents);
	}

	public Message startMessage()
	{
		return new Message("status.start." + this.condition.id).addReplacement("<pokemon>", this.pokemon.getNickname());
	}

	public void tick(Floor floor, ArrayList<DungeonEvent> events)
	{
		if (!this.isOver()) this.condition.tick(floor, this, events);
		++this.tick;
		this.actedWhileApplied = false;
		if (this.isOver()) this.finish(floor, events);
	}

}

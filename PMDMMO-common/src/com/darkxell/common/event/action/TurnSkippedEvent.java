package com.darkxell.common.event.action;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.stats.BellyChangedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public class TurnSkippedEvent extends DungeonEvent
{

	public final DungeonPokemon pokemon;

	public TurnSkippedEvent(Floor floor, DungeonPokemon pokemon)
	{
		super(floor, pokemon);
		this.pokemon = pokemon;
	}

	@Override
	public String loggerMessage()
	{
		return this.pokemon + " skipped its turn.";
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.pokemon.isTeamLeader()) this.resultingEvents.add(new BellyChangedEvent(this.floor, this.pokemon, -.1 * this.pokemon.energyMultiplier()));
		return super.processServer();
	}
}

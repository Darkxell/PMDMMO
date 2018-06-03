package com.darkxell.common.event.action;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.stats.BellyChangedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class TurnSkippedEvent extends DungeonEvent implements Communicable
{

	private DungeonPokemon pokemon;

	public TurnSkippedEvent(Floor floor)
	{
		super(floor);
	}

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

	@Override
	public void read(JsonObject value)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public JsonObject toJson()
	{
		return Json.object().add("actor", this.pokemon.originalPokemon.getData().id);
	}
}

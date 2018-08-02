package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.ability.Ability;

public class TriggeredAbilityEvent extends DungeonEvent
{

	public final Ability ability;
	public final DungeonPokemon pokemon;

	public TriggeredAbilityEvent(Floor floor, DungeonPokemon pokemon)
	{
		super(floor);
		this.pokemon = pokemon;
		this.ability = this.pokemon.ability();
	}

	@Override
	public String loggerMessage()
	{
		return this.pokemon + "'s " + this.ability.name() + " ability triggered.";
	}
	
	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.ability.hasTriggeredMessage()) this.messages.add(this.ability.triggeredMessage(this.pokemon));
		return super.processServer();
	}

}

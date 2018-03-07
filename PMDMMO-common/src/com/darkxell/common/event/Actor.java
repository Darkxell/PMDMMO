package com.darkxell.common.event;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;

public class Actor
{

	private boolean actedThisSubturn;
	/** The current ticking. When it exceeds actionTime, an action may be taken. */
	private float actionTick;
	/** The time this Actor needs to take an action. */
	private float actionTime;
	public final DungeonPokemon pokemon;

	public Actor(DungeonPokemon pokemon)
	{
		this.pokemon = pokemon;
		this.actedThisSubturn = false;
		this.onSpeedChange();
	}

	public boolean actedThisSubturn()
	{
		return this.actedThisSubturn;
	}

	public void cancelSubTurn()
	{
		--this.actionTick;
		if (this.actionTick < 0) this.actionTick += this.actionTime;
		this.actedThisSubturn = false;
	}

	@Override
	public int hashCode()
	{
		return this.pokemon.hashCode(); // Allows checking for already existing actor in the list.
	}

	public void onSpeedChange()
	{
		this.actionTime = GameTurn.SUB_TURNS / this.pokemon.stats.getMoveSpeed();
		this.actionTick = (this.actionTime / 2) % 1;
	}

	public void onTurnEnd(Floor floor, ArrayList<DungeonEvent> events)
	{
		this.actedThisSubturn = false;
		this.pokemon.onTurnStart(floor, events);
	}

	/** Called at each subturn. Should advance calculations.
	 * 
	 * @return true if this Pokémon acts during this subturn. */
	public boolean subTurn()
	{
		++this.actionTick;
		boolean acts = this.actionTick >= this.actionTime;
		this.actionTick %= this.actionTime;
		this.actedThisSubturn = acts;
		return acts;
	}

}

package com.darkxell.common.event;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;

public class Actor
{

	public static enum Action
	{
		ACTED,
		NO_ACTION,
		SKIPPED;
	}

	Action actionThisSubturn;
	/** The current ticking. When it exceeds actionTime, an action may be taken. */
	private float actionTick;
	/** The time this Actor needs to take an action. */
	private float actionTime;
	public final DungeonPokemon pokemon;
	private boolean subTurnTriggered;

	public Actor(DungeonPokemon pokemon)
	{
		this.pokemon = pokemon;
		this.actionThisSubturn = Action.NO_ACTION;
		this.onSpeedChange();
	}

	public void act()
	{
		this.actionThisSubturn = Action.ACTED;
	}

	public Action actionThisSubturn()
	{
		return this.actionThisSubturn;
	}

	/* public void cancelSubTurn() { --this.actionTick; if (this.actionTick < 0) this.actionTick += this.actionTime; this.actedThisSubturn = false; } */

	@Override
	public int hashCode()
	{
		return this.pokemon.hashCode(); // Allows checking for already existing actor in the list.
	}

	public boolean hasSubTurnTriggered()
	{
		return this.subTurnTriggered;
	}

	public void onSpeedChange()
	{
		this.actionTime = GameTurn.SUB_TURNS * 1f / this.pokemon.stats.getMoveSpeed();
		// this.actionTick = (this.actionTime / 2) % 1; Disabled to avoid being de-synchronization. Will look into it if needs to be brought back.
	}

	public void onTurnEnd(Floor floor, ArrayList<DungeonEvent> events)
	{
		this.actionThisSubturn = Action.NO_ACTION;
		this.pokemon.onTurnStart(floor, events);
	}

	public void skip()
	{
		this.actionThisSubturn = Action.SKIPPED;
	}

	/** Called at each subturn. Should advance calculations.
	 * 
	 * @return true if this Pokemon acts during this subturn. */
	public boolean subTurn()
	{
		++this.actionTick;
		boolean acts = this.actionTick >= this.actionTime;
		// Logger.i("Time for " + this.pokemon + " to play ! Tick now at " + this.actionTick + ", with action time of " + this.actionTime + ". Will it act? " + acts);
		this.actionTick %= this.actionTime;
		this.subTurnTriggered = true;
		return acts;
	}

	public void subTurnEnded()
	{
		this.subTurnTriggered = false;
	}

	@Override
	public String toString()
	{
		return this.pokemon.toString();
	}

}

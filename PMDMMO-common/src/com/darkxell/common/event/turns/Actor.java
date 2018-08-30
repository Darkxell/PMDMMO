package com.darkxell.common.event.turns;

import com.darkxell.common.pokemon.DungeonPokemon;

public class Actor
{

	public static enum Action
	{
		ACTED,
		NO_ACTION,
		SKIPPED;
	}

	private Action actionThisSubturn;
	public final DungeonPokemon pokemon;
	private boolean slowCountedThisSubturn = false;
	private double slowCounter = 0;

	public Actor(DungeonPokemon pokemon)
	{
		this.pokemon = pokemon;
		this.actionThisSubturn = Action.NO_ACTION;
	}

	public void act()
	{
		this.actionThisSubturn = Action.ACTED;
	}

	public Action actionThisSubturn()
	{
		return this.actionThisSubturn;
	}

	@Override
	public int hashCode()
	{
		return this.pokemon.hashCode(); // Allows checking for already existing actor in the list.
	}

	public void resetSlowCounter()
	{
		this.slowCounter = 0;
	}

	public void skip()
	{
		this.actionThisSubturn = Action.SKIPPED;
	}

	public boolean slowActs(double speed)
	{
		if (!this.slowCountedThisSubturn)
		{
			this.slowCounter += speed / GameTurn.SUB_TURNS;
			this.slowCountedThisSubturn = true;
		}
		return this.slowCounter >= 1;
	}

	public void subTurnEnd()
	{
		this.actionThisSubturn = Action.NO_ACTION;
		this.slowCountedThisSubturn = false;
		while (this.slowCounter >= 1)
			this.slowCounter -= 1;
	}

	@Override
	public String toString()
	{
		return this.pokemon.toString();
	}

	public void unskip()
	{
		this.actionThisSubturn = Action.NO_ACTION;
	}

}

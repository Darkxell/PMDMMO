package com.darkxell.common.ai;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;

/** Basic standing still AI to test the AI system. */
public class SkipTurnsAI extends AI
{

	public SkipTurnsAI(Floor floor, DungeonPokemon pokemon)
	{
		super(floor, pokemon);
	}

	@Override
	protected void update()
	{}

}

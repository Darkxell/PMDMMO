package com.darkxell.common.move;

import com.darkxell.common.pokemon.PokemonType;

public class BasicAttack extends Move
{

	public BasicAttack()
	{
		super(0, PokemonType.UNKNOWN, 0, Move.PHYSICAL, 0, 1, 100, MoveRange.Front, MoveTarget.Foes, 0, 0, true);
	}

}

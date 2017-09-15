package com.darkxell.common.move;

import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public class BasicAttack extends Move
{

	public BasicAttack()
	{
		super(0, null, 0, Move.PHYSICAL, 0, 1, 100, Move.FRONT, 0, 0, true);
	}

	@Override
	public Message[] getUseMessages(DungeonPokemon user)
	{
		return new Message[0];
	}

}

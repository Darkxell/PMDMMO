package com.darkxell.common.event.move;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;

public class MoveSelectionEvent extends DungeonEvent
{

	public final LearnedMove move;
	public final DungeonPokemon user;

	public MoveSelectionEvent(LearnedMove move, DungeonPokemon user)
	{
		super(move.move().getUseMessages(user));
		this.move = move;
		this.user = user;
	}

}

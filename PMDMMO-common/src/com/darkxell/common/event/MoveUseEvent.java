package com.darkxell.common.event;

import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.DungeonPokemon;

public class MoveUseEvent extends Event
{

	/** The move that was used. */
	public final Move move;
	/** The Targets of this Move. */
	private final MoveTarget[] targets;
	/** The Pokémon that used the move. */
	public final DungeonPokemon user;

	public MoveUseEvent(Move move, DungeonPokemon user, MoveTarget... targets)
	{
		this.move = move;
		this.targets = targets;
		this.user = user;
	}

	public MoveTarget[] targets()
	{
		return this.targets.clone();
	}

}

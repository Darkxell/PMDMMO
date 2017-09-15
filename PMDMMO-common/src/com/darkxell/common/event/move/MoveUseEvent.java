package com.darkxell.common.event.move;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;

public class MoveUseEvent extends DungeonEvent
{

	public final Floor floor;
	/** The move that was used. */
	public final LearnedMove move;
	/** The Targets of this Move. */
	public final DungeonPokemon target;
	/** The Pokémon that used the move. */
	public final DungeonPokemon user;

	public MoveUseEvent(LearnedMove move, DungeonPokemon user, DungeonPokemon target, Floor floor)
	{
		this.move = move;
		this.user = user;
		this.target = target;
		this.floor = floor;
	}

	@Override
	public DungeonEvent[] processServer()
	{
		this.move.setPP(this.move.getPP() - 1);
		return this.move.move().useOn(this.user, this.target, this.floor);
	}

}

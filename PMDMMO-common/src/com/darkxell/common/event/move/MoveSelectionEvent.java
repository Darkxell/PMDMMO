package com.darkxell.common.event.move;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;

public class MoveSelectionEvent extends DungeonEvent
{

	public final Floor floor;
	public final LearnedMove move;
	public final DungeonPokemon user;

	public MoveSelectionEvent(LearnedMove move, DungeonPokemon user, Floor floor)
	{
		super(move.move().getUseMessages(user));
		this.move = move;
		this.user = user;
		this.floor = floor;
	}

	@Override
	public DungeonEvent[] processServer()
	{
		return this.move.move().prepareUse(this.user, this.move, this.floor);
	}

}

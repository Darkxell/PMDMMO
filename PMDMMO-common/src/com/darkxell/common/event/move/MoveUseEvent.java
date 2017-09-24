package com.darkxell.common.event.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.Message;

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
	public ArrayList<DungeonEvent> processServer()
	{
		this.resultingEvents.addAll(this.move.move().useOn(this.user, this.target, this.floor));
		if (this.resultingEvents.size() == 0) this.resultingEvents.add(new MessageEvent(new Message("move.no_effect")));
		return super.processServer();
	}
}

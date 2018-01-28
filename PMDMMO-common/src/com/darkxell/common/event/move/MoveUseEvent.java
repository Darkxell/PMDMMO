package com.darkxell.common.event.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class MoveUseEvent extends DungeonEvent
{

	/** The Targets of this Move. */
	public final DungeonPokemon target;
	/** The move that was used. */
	public final MoveUse usedMove;

	public MoveUseEvent(Floor floor, MoveUse move, DungeonPokemon target)
	{
		super(floor);
		this.usedMove = move;
		this.target = target;
	}

	@Override
	public String loggerMessage()
	{
		return this.target + " received the effect of " + this.usedMove.move.move().name();
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.resultingEvents.addAll(this.usedMove.move.move().useOn(this.usedMove, this.target, this.floor));
		if (this.resultingEvents.size() == 0) this.resultingEvents.add(new MessageEvent(this.floor, new Message("move.no_effect")));
		this.resultingEvents.add(this.usedMove.getExperienceEvent());
		return super.processServer();
	}
}

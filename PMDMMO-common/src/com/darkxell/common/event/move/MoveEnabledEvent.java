package com.darkxell.common.event.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.LearnedMove;

public class MoveEnabledEvent extends DungeonEvent
{

	/** The value of its "enabled" property when changed. */
	public final boolean enabled;
	/** The modified move. */
	public final LearnedMove move;

	public MoveEnabledEvent(Floor floor, LearnedMove move, boolean enabled)
	{
		super(floor);
		this.move = move;
		this.enabled = enabled;
	}

	@Override
	public String loggerMessage()
	{
		return this.move.move() + " was " + (this.enabled ? "en" : "dis") + "abled.";
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.move.setEnabled(this.enabled);
		return super.processServer();
	}

}

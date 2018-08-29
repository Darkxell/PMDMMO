package com.darkxell.common.ai.states;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.AIState;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.turns.GameTurn;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.Logger;

public class AIStateChargedAttack extends AIState
{

	public final int moveID;

	public AIStateChargedAttack(AI ai, int moveID)
	{
		super(ai);
		this.moveID = moveID;
	}

	@Override
	public DungeonEvent takeAction()
	{
		LearnedMove m = null;
		GameTurn last = this.ai.floor.dungeon.getLastTurn();
		for (DungeonEvent e : last.events())
			if (e instanceof MoveSelectionEvent && ((MoveSelectionEvent) e).usedMove().user == this.ai.pokemon) m = ((MoveSelectionEvent) e).usedMove().move;
		if (m == null)
		{
			Logger.e("Pokemon used a Charged Attack, but the move wasn't found!");
			return new TurnSkippedEvent(this.ai.floor, this.ai.pokemon);
		}
		LearnedMove move = new LearnedMove(this.moveID);
		move.setAddedLevel(m.getAddedLevel());
		return new MoveSelectionEvent(this.ai.floor, move, this.ai.pokemon);
	}

}

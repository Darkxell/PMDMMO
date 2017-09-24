package com.darkxell.common.event.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.Message;

public class MoveSelectionEvent extends DungeonEvent
{

	public final Floor floor;
	public final LearnedMove move;
	public final DungeonPokemon user;

	public MoveSelectionEvent(LearnedMove move, DungeonPokemon user, Floor floor)
	{
		this.move = move;
		this.user = user;
		this.floor = floor;

		if (this.move.move() != MoveRegistry.ATTACK) this.messages.add(new Message("move.used").addReplacement("<pokemon>", user.pokemon.getNickname())
				.addReplacement("<move>", this.move.move().name()));
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.move.setPP(this.move.getPP() - 1);
		this.resultingEvents.addAll(this.move.move().prepareUse(this.user, this.move, this.floor));
		return super.processServer();
	}

}

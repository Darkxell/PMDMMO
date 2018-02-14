package com.darkxell.common.event.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class MoveLearnedEvent extends DungeonEvent
{

	public final int index;
	public final Move move;
	public final Pokemon pokemon;

	public MoveLearnedEvent(Floor floor, Pokemon pokemon, Move move, int index)
	{
		super(floor);
		this.pokemon = pokemon;
		this.move = move;
		this.index = index;

		this.messages.add(new Message("moves.learned").addReplacement("<pokemon>", this.pokemon.getNickname()).addReplacement("<move>", this.move.name()));
	}

	@Override
	public String loggerMessage()
	{
		return this.pokemon + " learned " + this.move;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.pokemon.setMove(this.index, new LearnedMove(this.move.id));
		return super.processServer();
	}

}

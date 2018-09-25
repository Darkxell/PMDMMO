package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class PPRestoredEvent extends DungeonEvent
{

	public final DungeonPokemon pokemon;
	public final int pp;

	public PPRestoredEvent(Floor floor, DungeonPokemon pokemon, int pp)
	{
		super(floor);
		this.pokemon = pokemon;
		this.pp = pp;
	}

	@Override
	public String loggerMessage()
	{
		return this.pokemon + " had its PP resotred by " + this.pp;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.messages.add(new Message("moves.pp_restored").addReplacement("<pokemon>", this.pokemon.getNickname()));
		for (int m = 0; m < this.pokemon.moveCount(); ++m)
			this.pokemon.move(m).setPP(this.pokemon.move(m).pp() + this.pp);
		return super.processServer();
	}

}

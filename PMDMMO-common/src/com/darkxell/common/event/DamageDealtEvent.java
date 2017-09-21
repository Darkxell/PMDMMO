package com.darkxell.common.event;

import java.util.ArrayList;

import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public class DamageDealtEvent extends DungeonEvent
{

	public final int damage;
	public final DungeonPokemon target;

	public DamageDealtEvent(DungeonPokemon target, int damage)
	{
		this.target = target;
		this.damage = damage;

		this.messages.add(new Message("move.damage_dealt").addReplacement("<pokemon>", target.pokemon.getNickname()).addReplacement("<amount>",
				Integer.toString(damage)));
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.target.setHP(this.target.getHp() - this.damage);
		ArrayList<DungeonEvent> events = super.processServer();
		if (this.target.getHp() == 0) events.add(new FaintedPokemonEvent(this.target));
		return events;
	}

}

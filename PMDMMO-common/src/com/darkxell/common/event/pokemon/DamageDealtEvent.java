package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public class DamageDealtEvent extends DungeonEvent
{

	public final int damage;
	/** The Pok�mon that dealt damage. Can be null if the damage didn't result from a Pok�mon's move. */
	public final DungeonPokemon damager;
	public final DungeonPokemon target;

	public DamageDealtEvent(DungeonPokemon target, DungeonPokemon damager, int damage)
	{
		this.target = target;
		this.damager = damager;
		this.damage = damage;

		this.messages.add(new Message("move.damage_dealt").addReplacement("<pokemon>", target.pokemon.getNickname()).addReplacement("<amount>",
				Integer.toString(damage)));
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.target.setHP(this.target.getHp() - this.damage);
		ArrayList<DungeonEvent> events = super.processServer();
		if (this.target.getHp() == 0) events.add(new FaintedPokemonEvent(this.target, this.damager));
		return events;
	}

}

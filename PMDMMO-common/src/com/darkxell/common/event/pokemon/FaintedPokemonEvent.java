package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.dungeon.BossDefeatedEvent;
import com.darkxell.common.event.dungeon.PlayerLosesEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class FaintedPokemonEvent extends DungeonEvent
{

	/** The source that damaged the fainted Pok�mon. Can be null if the fainting damage didn't result from a Pok�mon's move. */
	public final DamageSource damage;
	public final DungeonPokemon pokemon;

	public FaintedPokemonEvent(Floor floor, DungeonPokemon pokemon, DamageSource damage)
	{
		super(floor);
		this.pokemon = pokemon;
		this.damage = damage;
	}

	@Override
	public String loggerMessage()
	{
		return this.messages.get(0).toString();
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.messages.add(new Message("pokemon.fainted").addReplacement("<pokemon>", pokemon.getNickname()));

		if (this.pokemon.getItem() != null) this.pokemon.tile().setItem(this.pokemon.getItem());
		if (this.damage.getExperienceEvent() != null) this.damage.getExperienceEvent().experience += this.pokemon.experienceGained();
		this.floor.unsummonPokemon(this.pokemon);
		if (this.pokemon.isTeamLeader())
		{
			int moveID = -1;
			if (this.damage != null && this.damage instanceof MoveUse) moveID = ((MoveUse) this.damage).move.moveId();
			this.resultingEvents.add(new PlayerLosesEvent(this.floor, this.pokemon.originalPokemon.player(), moveID));
		}

		if (this.pokemon.isBoss)
		{
			boolean wasLastBoss = true;
			for (DungeonPokemon p : this.floor.listPokemon())
				if (p.isBoss)
				{
					wasLastBoss = false;
					break;
				}

			if (wasLastBoss) this.resultingEvents.add(new BossDefeatedEvent(this.floor));
		}

		return super.processServer();
	}
}

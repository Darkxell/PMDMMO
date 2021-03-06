package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.event.dungeon.BossDefeatedEvent;
import com.darkxell.common.event.dungeon.PlayerLosesEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.util.language.Message;

public class FaintedPokemonEvent extends Event {

    /** The source that damaged the fainted Pokemon. Can be null if the fainting damage didn't result from a Pokemon's move. */
    public final DamageSource damage;
    public final DungeonPokemon pokemon;

	public FaintedPokemonEvent(Floor floor, EventSource eventSource, DungeonPokemon pokemon, DamageSource damage) {
		super(floor, eventSource);
		this.pokemon = pokemon;
		this.damage = damage;
	}

	@Override
	public String loggerMessage() {
		return this.pokemon + " fainted";
	}

	@Override
	public ArrayList<Event> processServer() {
		this.messages.add(new Message("pokemon.fainted").addReplacement("<pokemon>", pokemon.getNickname()));

		if (this.damage instanceof MoveUse) {
			DungeonPokemon user = ((MoveUse) this.damage).user;
			boolean canRecruit = user.isTeamLeader()
					&& user.tile().adjacentTile(user.facing()).getPokemon() == this.pokemon;
			if (user.isTeamLeader())
				canRecruit &= RecruitAttemptEvent.checkFriendArea(user, this.pokemon);
			if (canRecruit)
				this.resultingEvents
						.add(new RecruitAttemptEvent(this.floor, this, ((MoveUse) this.damage).user, this.pokemon));
		}

		if (this.pokemon.hasItem())
			this.pokemon.tile().setItem(this.pokemon.getItem()); //FIXME: Replace this with a ItemLandedEvent
		if (this.damage!=null && this.damage.getExperienceEvent() != null)
			this.damage.getExperienceEvent().experience += this.pokemon.experienceGained();
		this.floor.unsummonPokemon(this.pokemon);
		if (this.pokemon.type == DungeonPokemonType.TEAM_MEMBER) {
			int moveID = -1;
			if (this.damage != null && this.damage instanceof MoveUse)
				moveID = ((MoveUse) this.damage).move.moveId();
			this.resultingEvents
					.add(new PlayerLosesEvent(this.floor, this, this.pokemon.originalPokemon.player(), moveID));
		}

		if (this.pokemon.type == DungeonPokemonType.BOSS) {
			boolean wasLastBoss = true;
			for (DungeonPokemon p : this.floor.listPokemon())
				if (p.type == DungeonPokemonType.BOSS) {
					wasLastBoss = false;
					break;
				}

			if (wasLastBoss)
				this.resultingEvents.add(new BossDefeatedEvent(this.floor, this));
		}

		return super.processServer();
	}
}

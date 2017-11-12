package com.darkxell.common.event.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.BellyChangedEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.event.stats.ExperienceGeneratedEvent;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.language.Message;

public class MoveSelectionEvent extends DungeonEvent
{

	public static class MoveUse implements DamageSource
	{
		/** The experience event resulting from this move's use. */
		private final ExperienceGeneratedEvent experienceEvent;
		public final LearnedMove move;
		public final DungeonPokemon user;

		public MoveUse(Floor floor, LearnedMove move, DungeonPokemon user)
		{
			this.move = move;
			this.user = user;
			this.experienceEvent = this.user.pokemon.player == null ? null : new ExperienceGeneratedEvent(floor, this.user.pokemon.player);
		}

		@Override
		public ExperienceGeneratedEvent getExperienceEvent()
		{
			return this.experienceEvent;
		}
	}

	public final MoveUse usedMove;

	public MoveSelectionEvent(Floor floor, LearnedMove move, DungeonPokemon user)
	{
		super(floor, user);
		this.usedMove = new MoveUse(floor, move, user);

		if (this.usedMove.move.move() != MoveRegistry.ATTACK) this.messages.add(new Message("move.used")
				.addReplacement("<pokemon>", user.pokemon.getNickname()).addReplacement("<move>", this.usedMove.move.move().name()));
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.usedMove.move.setPP(this.usedMove.move.getPP() - 1);
		if (this.usedMove.user.isTeamLeader()) this.resultingEvents.add(new BellyChangedEvent(this.floor, this.usedMove.user,
				-(this.usedMove.move.isLinked() ? .9 : .1) * this.usedMove.user.energyMultiplier()));
		this.resultingEvents.addAll(this.usedMove.move.move().prepareUse(this.usedMove, this.floor));
		return super.processServer();
	}

}

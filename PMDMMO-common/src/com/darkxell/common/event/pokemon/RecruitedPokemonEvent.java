package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.util.language.Message;

public class RecruitedPokemonEvent extends Event {

	public final DungeonPokemon recruiter, recruit;

	public RecruitedPokemonEvent(Floor floor, EventSource eventSource, DungeonPokemon recruiter,
			DungeonPokemon recruit) {
		super(floor, eventSource);
		this.recruiter = recruiter;
		this.recruit = recruit;
	}

	@Override
	public String loggerMessage() {
		return this.recruit + " is recruited by " + this.recruiter;
	}

	@Override
	public ArrayList<Event> processServer() {
		boolean fainted = this.recruit.isFainted();

		this.recruit.revive();
		this.recruiter.player().addAlly(this.recruit.originalPokemon);
		this.recruit.type = DungeonPokemonType.TEAM_MEMBER;

		if (fainted)
		{
			this.recruit.tile().setPokemon(this.recruit);
			this.floor.dungeon.registerActor(this.recruit);
		}
		else
			this.floor.aiManager.unregister(this.recruit); // Reset AI to partner
		this.floor.aiManager.register(this.recruit);

		this.messages.add(new Message("recruit.success").addReplacement("<pokemon>", this.recruit.getNickname())
				.addReplacement("<player>", this.recruit.player().name()));
		return super.processServer();
	}

}

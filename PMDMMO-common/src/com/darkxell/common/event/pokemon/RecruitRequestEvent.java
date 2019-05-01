package com.darkxell.common.event.pokemon;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.pokemon.DungeonPokemon;

public class RecruitRequestEvent extends Event {

	public final DungeonPokemon recruiter, recruit;

	public RecruitRequestEvent(Floor floor, EventSource eventSource, DungeonPokemon recruiter, DungeonPokemon recruit) {
		super(floor, eventSource);
		this.recruiter = recruiter;
		this.recruit = recruit;
	}

	@Override
	public String loggerMessage() {
		return this.recruit + " would like to join " + this.recruiter + "'s team";
	}

}

package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class RecruitAttemptEvent extends Event {

	public final DungeonPokemon recruiter, target;

	public RecruitAttemptEvent(Floor floor, EventSource eventSource, DungeonPokemon recruiter, DungeonPokemon target) {
		super(floor, eventSource);
		this.recruiter = recruiter;
		this.target = target;
	}

	@Override
	public boolean isValid() {
		return this.recruiter.isTeamLeader();
	}

	@Override
	public String loggerMessage() {
		return this.recruiter + " attempts to recruit " + target;
	}

	@Override
	public ArrayList<Event> processServer() {

		boolean success = this.floor.random.nextDouble() < this.recruitChance();
		if (success)
			this.resultingEvents.add(new RecruitedPokemonEvent(this.floor, this, this.recruiter, this.target));
		else if (!this.target.isFainted()) {
			this.messages.add(new Message("recruit.fail").addReplacement("<pokemon>", this.target.getNickname()));
			Stat raisedStat = this.floor.random.nextDouble() < .5 ? Stat.Attack : Stat.Defense;
			this.resultingEvents.add(new StatChangedEvent(this.floor, this, this.target, raisedStat, 1));
		}

		return super.processServer();
	}

	private double recruitChance() {
		return 1; // TODO compute recruit chance
	}

}

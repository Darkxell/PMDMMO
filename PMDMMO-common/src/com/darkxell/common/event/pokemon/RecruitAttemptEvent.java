package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.zones.FriendAreaAcquisition.BaseFriendAreaAcquisition;

public class RecruitAttemptEvent extends Event {

	public static boolean checkFriendArea(DungeonPokemon recruiter, DungeonPokemon recruit) {
		return true;
		/*return recruit.species().friendArea().acquisition != BaseFriendAreaAcquisition.ON_RECRUIT
				&& (recruiter.player().friendAreas == null
						|| !recruiter.player().friendAreas.contains(recruit.species().friendArea()));*/
	}

	public final DungeonPokemon recruiter, target;
	private boolean success = false;

	public RecruitAttemptEvent(Floor floor, EventSource eventSource, DungeonPokemon recruiter, DungeonPokemon target) {
		super(floor, eventSource);
		this.recruiter = recruiter;
		this.target = target;
	}

	public boolean hasSucceeded() {
		return this.success;
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
		boolean fromFaint = this.target.isFainted();

		this.success = this.floor.random.nextDouble() < this.recruitChance();
		if (this.success) {
			if (fromFaint)
				this.resultingEvents.add(new RecruitRequestEvent(this.floor, this, this.recruiter, this.target));
			else
				this.resultingEvents.add(new RecruitedPokemonEvent(this.floor, this, this.recruiter, this.target));
		} else if (!fromFaint) {
			this.messages.add(new Message("recruit.fail").addReplacement("<pokemon>", this.target.getNickname()));
			Stat raisedStat = this.floor.random.nextDouble() < .5 ? Stat.Attack : Stat.Defense;
			this.resultingEvents.add(new StatChangedEvent(this.floor, this, this.target, raisedStat, 1));
		}

		return super.processServer();
	}

	private double recruitChance() {
		return 1; // TODO RECRUIT: compute recruit chance
	}

}

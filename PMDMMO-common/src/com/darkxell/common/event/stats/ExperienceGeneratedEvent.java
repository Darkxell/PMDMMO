package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.Pokemon;

public class ExperienceGeneratedEvent extends DungeonEvent
{

	/** The experience gained. */
	public int experience;
	/** The Player whose Pokémon will gain experience. */
	public final Player player;

	public ExperienceGeneratedEvent(Floor floor, Player player)
	{
		super(floor);
		this.player = player;
		this.experience = 0;
		this.priority = PRIORITY_AFTER_MOVE;
	}

	@Override
	public String loggerMessage()
	{
		return null;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.experience > 0) for (Pokemon member : this.player.getTeam())
			this.resultingEvents.add(new ExperienceGainedEvent(this.floor, member, this.experience));
		return super.processServer();
	}

}

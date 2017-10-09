package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class ExperienceGainedEvent extends DungeonEvent
{

	public final int experience;
	/** The amount of levels that were passed with this experience gain. */
	private int levelsup;
	public final Pokemon pokemon;

	public ExperienceGainedEvent(Floor floor, Pokemon pokemon, int experience)
	{
		super(floor);
		this.pokemon = pokemon;
		this.experience = experience;

		this.messages.add(new Message("xp.gain").addReplacement("<pokemon>", this.pokemon.getNickname()).addReplacement("<xp>",
				Integer.toString(this.experience)));
	}

	public int levelsup()
	{
		return this.levelsup;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.levelsup = this.pokemon.getLevel();
		this.pokemon.gainExperience(this.experience);
		this.levelsup = this.pokemon.getLevel() - this.levelsup;
		return super.processServer();
	}

}

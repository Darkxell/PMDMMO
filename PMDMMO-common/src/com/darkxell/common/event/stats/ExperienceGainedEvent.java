package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public class ExperienceGainedEvent extends DungeonEvent
{

	public final int experience;
	/** The amount of levels that were passed with this experience gain. */
	private int levelsup;
	public final DungeonPokemon pokemon;

	public ExperienceGainedEvent(DungeonPokemon pokemon, int experience)
	{
		this.pokemon = pokemon;
		this.experience = experience;

		this.messages.add(new Message("xp.gain").addReplacement("<pokemon>", this.pokemon.pokemon.getNickname()).addReplacement("<xp>",
				Integer.toString(this.experience)));
	}

	public int levelsup()
	{
		return this.levelsup;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.levelsup = this.pokemon.pokemon.getLevel();
		this.pokemon.pokemon.gainExperience(this.experience);
		this.levelsup = this.pokemon.pokemon.getLevel() - this.levelsup;
		return super.processServer();
	}

}

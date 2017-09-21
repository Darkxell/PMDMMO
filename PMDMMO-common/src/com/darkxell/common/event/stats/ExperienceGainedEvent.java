package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public class ExperienceGainedEvent extends DungeonEvent
{

	public final int experience;
	public final DungeonPokemon pokemon;

	public ExperienceGainedEvent(DungeonPokemon pokemon, int experience)
	{
		this.pokemon = pokemon;
		this.experience = experience;

		this.messages.add(new Message("xp.gain").addReplacement("<pokemon>", this.pokemon.pokemon.getNickname()).addReplacement("<xp>",
				Integer.toString(this.experience)));
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.pokemon.pokemon.gainExperience(this.experience);
		return super.processServer();
	}

}

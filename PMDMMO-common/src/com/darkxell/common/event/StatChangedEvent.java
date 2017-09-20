package com.darkxell.common.event;

import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public class StatChangedEvent extends DungeonEvent
{
	public static final String[] MESSAGES = new String[]
	{ "stat.decrease.3", "stat.decrease.2", "stat.decrease.1", null, "stat.increase.1", "stat.increase.2", "stat.increase.3", };

	public final int stage;
	public final int stat;
	public final DungeonPokemon target;

	public StatChangedEvent(DungeonPokemon target, int stat, int stage)
	{
		this.target = target;
		this.stat = stat;
		this.stage = stage;

		this.messages.add(new Message(MESSAGES[this.stage + 3]).addReplacement("<pokemon>", this.target.pokemon.getNickname()).addReplacement("<stat>",
				new Message("stat." + this.stat)));
	}

	@Override
	public DungeonEvent[] processServer()
	{
		this.target.stats.addStage(this.stat, this.stage);
		return super.processServer();
	}
}

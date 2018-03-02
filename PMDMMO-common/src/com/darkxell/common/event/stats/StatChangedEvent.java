package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class StatChangedEvent extends DungeonEvent
{
	public static final String[] MESSAGES = new String[] { "stat.decrease.3", "stat.decrease.2", "stat.decrease.1", null, "stat.increase.1", "stat.increase.2",
			"stat.increase.3", };

	public final int stage;
	public final int stat;
	public final DungeonPokemon target;

	public StatChangedEvent(Floor floor, DungeonPokemon target, int stat, int stage)
	{
		super(floor);
		this.target = target;
		this.stat = stat;
		this.stage = stage;

		this.messages.add(new Message(MESSAGES[this.stage + 3]).addReplacement("<pokemon>", this.target.getNickname()).addReplacement("<stat>",
				new Message("stat." + this.stat)));
	}

	@Override
	public boolean isValid()
	{
		return !this.target.isFainted();
	}

	@Override
	public String loggerMessage()
	{
		return this.messages.get(0).toString();
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.target.stats.addStage(this.stat, this.stage);
		return super.processServer();
	}
}

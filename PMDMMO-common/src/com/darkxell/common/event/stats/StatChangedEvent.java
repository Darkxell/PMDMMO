package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class StatChangedEvent extends DungeonEvent
{
	public static final String[] MESSAGES = new String[] { "stat.decrease.3", "stat.decrease.2", "stat.decrease.1", null, "stat.increase.1", "stat.increase.2",
			"stat.increase.3", };

	public final int stage;
	public final Stat stat;
	public final DungeonPokemon target;

	public StatChangedEvent(Floor floor, DungeonPokemon target, Stat stat, int stage)
	{
		super(floor);
		this.target = target;
		this.stat = stat;
		this.stage = stage;
	}

	@Override
	public boolean isValid()
	{
		return !this.target.isFainted();
	}

	@Override
	public String loggerMessage()
	{
		return this.target + " has its " + this.stat + " changed by " + this.stage;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		int effective = this.target.stats.effectiveChange(this.stat, this.stage);

		if (effective != 0) this.target.stats.addStage(this.stat, effective);
		if (this.stat == Stat.Speed && effective != 0) this.floor.dungeon.onSpeedChange(this.target);

		String messageID = MESSAGES[effective + 3];
		if (effective == 0)
		{
			if (this.stage > 0) messageID = "stat.increase.fail";
			else messageID = "stat.decrease.fail";
		} else if (this.stat == Stat.Speed) messageID = "stat.speed." + String.valueOf(this.target.stats.getMoveSpeed()).substring(0, 1);
		this.messages
				.add(new Message(messageID).addReplacement("<pokemon>", this.target.getNickname()).addReplacement("<stat>", new Message("stat." + this.stat)));

		return super.processServer();
	}
}

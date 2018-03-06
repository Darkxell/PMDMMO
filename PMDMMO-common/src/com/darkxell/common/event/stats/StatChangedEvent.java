package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonStats;
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
		if (this.stat == PokemonStats.SPEED) this.floor.dungeon.onSpeedChange(this.target, this.stage);

		this.messages.add(new Message(
				this.stat == PokemonStats.SPEED ? "stat.speed." + String.valueOf(this.target.stats.getMoveSpeed()).substring(0, 1) : MESSAGES[this.stage + 3])
						.addReplacement("<pokemon>", this.target.getNickname()).addReplacement("<stat>", new Message("stat." + this.stat)));

		return super.processServer();
	}
}

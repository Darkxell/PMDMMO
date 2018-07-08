package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.BaseStats.Stat;

public class StatChangeEffect extends MoveEffect
{

	public final int stage;
	public final Stat stat;

	public StatChangeEffect(int id, Stat stat, int stage)
	{
		super(id);
		this.stat = stat;
		this.stage = stage;
	}

	@Override
	protected void useOn(MoveUse usedMove, DungeonPokemon target, Floor floor, boolean missed, ArrayList<DungeonEvent> events)
	{
		super.useOn(usedMove, target, floor, missed, events);

		if (!missed) events.add(new StatChangedEvent(floor, target, this.stat, this.stage));
	}

}

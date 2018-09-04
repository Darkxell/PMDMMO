package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.BaseStats.Stat;

public class StatChangeEffect extends RandomStatChangeEffect
{

	public final Stat stat;

	public StatChangeEffect(int id, Stat stat, int stage, int probability)
	{
		super(id, stage, probability);
		this.stat = stat;
	}

	@Override
	protected Stat stat(Floor floor)
	{
		return this.stat;
	}

}

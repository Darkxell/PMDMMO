package com.darkxell.common.item.effects;

import com.darkxell.common.pokemon.BaseStats.Stat;

/** An Item that increases a stat when drunk. */
public class StatBoostDrinkItemEffect extends DrinkItemEffect
{

	public final Stat stat;

	public StatBoostDrinkItemEffect(int id, int food, int bellyIfFull, int belly, Stat stat)
	{
		super(id, food, bellyIfFull, belly);
		this.stat = stat;
	}
}

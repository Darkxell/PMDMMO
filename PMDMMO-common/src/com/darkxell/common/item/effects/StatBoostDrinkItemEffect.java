package com.darkxell.common.item.effects;

/** An Item that increases a stat when drunk. */
public class StatBoostDrinkItemEffect extends DrinkItemEffect
{

	public final byte stat;

	public StatBoostDrinkItemEffect(int id, int food, int bellyIfFull, int belly, byte stat)
	{
		super(id, food, bellyIfFull, belly);
		this.stat = stat;
	}
}

package com.darkxell.common.item.effects;

/** An Item that restores PP when eaten. */
public class ElixirItemEffect extends DrinkItemEffect
{

	public final int pp;

	public ElixirItemEffect(int id, int food, int bellyIfFull, int belly, int pp)
	{
		super(id, food, bellyIfFull, belly);
		this.pp = pp;
	}

	@Override
	public boolean isUsedOnTeamMember()
	{
		return true;
	}

}

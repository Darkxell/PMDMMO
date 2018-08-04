package com.darkxell.common.item.effects;

/** An Item that restores belly when eaten, and inflicts negative status effects. */
public class HealFoodItemEffect extends FoodItemEffect
{

	/** The amount of health this Item restores when eaten. */
	public final int hp;
	/** The amount of health this Item adds to the maximum health when eaten with full health. */
	public final int hpFull;

	public HealFoodItemEffect(int id, int food, int bellyIfFull, int belly, int hp, int hpFull)
	{
		super(id, food, bellyIfFull, belly);
		this.hp = hp;
		this.hpFull = hpFull;
	}

	@Override
	public boolean isUsedOnTeamMember()
	{
		return true;
	}

}

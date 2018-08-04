package com.darkxell.common.item.effects;

import com.darkxell.common.item.Item.ItemCategory;
import com.darkxell.common.item.ItemEffect;
import com.darkxell.common.util.language.Message;

public class ThrowableItemEffect extends ItemEffect
{
	public static enum ThrowableTrajectory
	{
		Arc,
		Straight
	}

	/** The damage this throwable Item deals. */
	public final int damage;
	/** The type of the trajectory. */
	public final ThrowableTrajectory trajectory;

	public ThrowableItemEffect(int id, int damage, ThrowableTrajectory trajectory)
	{
		super(id);
		this.damage = damage;
		this.trajectory = trajectory;
	}

	public ItemCategory category()
	{
		return ItemCategory.THROWABLE;
	}

	@Override
	public Message getUseName()
	{
		return new Message("item.throw");
	}

	@Override
	public boolean isThrowable()
	{
		return false;
	}

	@Override
	public boolean isUsable()
	{
		return true;
	}

}

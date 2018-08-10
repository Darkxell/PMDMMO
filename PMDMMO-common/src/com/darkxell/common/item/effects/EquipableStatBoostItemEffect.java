package com.darkxell.common.item.effects;

import com.darkxell.common.item.ItemEffect;

/** An Item that boosts a stat when hold. */
public class EquipableStatBoostItemEffect extends ItemEffect
{

	/** The boosted stat. */
	public final byte stat;

	public EquipableStatBoostItemEffect(int id, byte stat)
	{
		super(id);
		this.stat = stat;
	}

}

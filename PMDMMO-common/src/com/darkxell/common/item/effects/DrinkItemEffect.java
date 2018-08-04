package com.darkxell.common.item.effects;

import com.darkxell.common.util.language.Message;

/** An Item that Increases the power of selected move by 1, and rarely 3. */
public class DrinkItemEffect extends FoodItemEffect
{

	public DrinkItemEffect(int id, int food, int bellyIfFull, int belly)
	{
		super(id, food, bellyIfFull, belly);
	}

	@Override
	protected String getUseEffectID()
	{
		return "item.ingested";
	}

	@Override
	public Message getUseName()
	{
		return new Message("item.ingest");
	}

}

package com.darkxell.common.item.effects;

import com.darkxell.common.Registries;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemEffect;
import com.darkxell.common.move.Move;
import com.darkxell.common.util.language.Message;

public class TeachedMoveItemEffect extends ItemEffect
{

	public final int moveID;

	public TeachedMoveItemEffect(int id, int moveID)
	{
		super(id);
		this.moveID = moveID;
	}

	@Override
	public Message description(Item item)
	{
		return new Message("item.info.used_tm").addReplacement("<move>", this.move().name());
	}

	public Move move()
	{
		return Registries.moves().find(this.moveID);
	}

	@Override
	public Message name(Item item)
	{
		return new Message("move." + this.moveID).addPrefix("<tmu>");
	}

}

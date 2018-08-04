package com.darkxell.common.item.effects;

import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemEffect;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.util.language.Message;

public class TeachedMoveItemEffect extends ItemEffect
{

	public final int moveID;

	public TeachedMoveItemEffect(int id, int moveID)
	{
		super(id);
		this.moveID = moveID;
	}

	public Move move()
	{
		return MoveRegistry.find(this.moveID);
	}

	@Override
	public Message name(Item item)
	{
		return new Message("move." + this.moveID).addPrefix("<tmu>");
	}

}

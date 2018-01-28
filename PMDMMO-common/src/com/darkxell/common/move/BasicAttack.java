package com.darkxell.common.move;

import com.darkxell.common.util.language.Message;

public class BasicAttack extends Move
{

	public BasicAttack()
	{
		super(0, null, 0, Move.PHYSICAL, 0, 1, 100, Move.FRONT, 0, 0, true, false);
	}

	@Override
	public Message name()
	{
		return new Message("Basic attack", false);
	}

}

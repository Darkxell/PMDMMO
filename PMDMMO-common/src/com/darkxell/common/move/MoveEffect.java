package com.darkxell.common.move;

public class MoveEffect
{

	public final int id;

	public MoveEffect(int id)
	{
		this.id = id;
		MoveEffects.effects.put(this.id, this);
	}

}

package com.darkxell.client.state.menu.dungeon;

import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.InfoState;
import com.darkxell.common.move.Move;
import com.darkxell.common.util.language.Message;

public class MoveInfoState extends InfoState
{

	public MoveInfoState(Move move, AbstractState background, AbstractState parent)
	{
		super(background, parent, new Message[]
		{ move.name() }, new Message[]
		{ move.description() });
	}

}

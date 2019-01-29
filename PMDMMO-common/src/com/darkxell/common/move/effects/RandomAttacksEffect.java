package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Message;

public class RandomAttacksEffect extends MoveEffect
{

	public final int attacks;

	public RandomAttacksEffect(int id, int attacks)
	{
		super(id);
		this.attacks = attacks;
	}

	@Override
	public void createMoves(MoveUse move, Floor floor, ArrayList<DungeonEvent> events)
	{
		for (int i = 0; i < this.attacks; ++i)
		{
			Direction d = RandomUtil.random(Direction.DIRECTIONS, floor.random);
			MoveUseEvent e = new MoveUseEvent(floor, move, move.user.tile().adjacentTile(d).getPokemon());
			e.direction = d;
			events.add(e);
		}
	}

	@Override
	public Message descriptionBase(Move move)
	{
		return new Message("move.info.random_attacks").addReplacement("<attacks>", String.valueOf(this.attacks));
	}

}

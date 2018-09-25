package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.language.Message;

/** An Item that has different effects when used. */
public class OrbItemEffect extends ItemEffect
{
	/** ID of the Move used when using this Orb. */
	public final int moveID;

	public OrbItemEffect(int id, int moveID)
	{
		super(id);
		this.moveID = moveID;
	}

	@Override
	public boolean isUsable()
	{
		return true;
	}

	@Override
	public Message name(Item item)
	{
		return super.name(item).addPrefix("<orb>");
	}

	@Override
	public final void use(Floor floor, Item item, DungeonPokemon pokemon, DungeonPokemon target, ArrayList<DungeonEvent> events)
	{
		if (floor.data.isBossFloor()) events.add(new MessageEvent(floor, new Message("item.orb.boss")));
		else events.add(new MoveSelectionEvent(floor, new LearnedMove(this.moveID), pokemon));
	}

}

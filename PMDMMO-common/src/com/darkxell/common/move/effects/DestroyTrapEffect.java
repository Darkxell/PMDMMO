package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.dungeon.TrapDestroyedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;

public class DestroyTrapEffect extends MoveEffect
{

	public DestroyTrapEffect(int id)
	{
		super(id);
	}

	@Override
	public void additionalEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor, MoveEffectCalculator calculator, boolean missed, MoveEvents effects)
	{
		super.additionalEffects(usedMove, target, flags, floor, calculator, missed, effects);

		if (!missed)
		{
			Tile t = usedMove.user.tile().adjacentTile(usedMove.user.facing());
			if (t != null && t.trap != null) effects.createEffect(new TrapDestroyedEvent(floor, t), usedMove, target, floor, missed, true, null);
		}
	}

	@Override
	protected boolean allowsNoTarget(Move move, DungeonPokemon user)
	{
		Tile t = user.tile().adjacentTile(user.facing());
		return t.trap != null;
	}

}

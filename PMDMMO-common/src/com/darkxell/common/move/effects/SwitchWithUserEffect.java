package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.SwitchedPokemonEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;

public class SwitchWithUserEffect extends MoveEffect
{

	public SwitchWithUserEffect(int id)
	{
		super(id);
	}

	@Override
	public void additionalEffects(MoveUse usedMove, DungeonPokemon target, Floor floor, MoveEffectCalculator calculator, boolean missed, MoveEvents effects)
	{
		super.additionalEffects(usedMove, target, floor, calculator, missed, effects);

		if (!missed) effects.createEffect(new SwitchedPokemonEvent(floor, usedMove.user, target), usedMove, target, floor, missed, false, target);
	}

}

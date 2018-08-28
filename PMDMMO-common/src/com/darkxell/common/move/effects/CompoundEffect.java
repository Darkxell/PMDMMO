package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;

public class CompoundEffect extends MoveEffect
{

	private MoveEffect[] effects;

	public CompoundEffect(int id, MoveEffect... effects)
	{
		super(id);
		this.effects = effects;
	}

	@Override
	public void additionalEffects(MoveUse usedMove, DungeonPokemon target, Floor floor, MoveEffectCalculator calculator, boolean missed, MoveEvents effects)
	{
		super.additionalEffects(usedMove, target, floor, calculator, missed, effects);

		for (MoveEffect e : this.effects)
			e.additionalEffects(usedMove, target, floor, calculator, missed, effects);
	}

}

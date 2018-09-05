package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;

public class ApplyStatusConditionEffect extends MoveEffect
{

	public final int probability;
	public final StatusCondition status;

	public ApplyStatusConditionEffect(int id, StatusCondition status, int probability)
	{
		super(id);
		this.status = status;
		this.probability = probability;
	}

	@Override
	public void additionalEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor, MoveEffectCalculator calculator, boolean missed, MoveEvents effects)
	{
		super.additionalEffects(usedMove, target, flags, floor, calculator, missed, effects);

		if (!missed && floor.random.nextDouble() * 100 < this.probability)
			effects.createEffect(new StatusConditionCreatedEvent(floor, this.status.create(target, usedMove.user, floor.random)), usedMove, target, floor,
					missed, usedMove.move.move().dealsDamage, target);
	}

}

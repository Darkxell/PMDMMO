package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;

public class WrapStatusConditionEffect extends MoveEffect {

	public WrapStatusConditionEffect(int id) {
		super(id);
	}

	@Override
	protected void mainEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor,
			MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
		super.mainEffects(usedMove, target, flags, floor, calculator, missed, effects);

		if (!missed) {
			boolean willSucceed = !usedMove.user.hasStatusCondition(StatusConditions.Wrapping)
					&& !target.hasStatusCondition(StatusConditions.Wrapped);

			if (willSucceed) {
				AppliedStatusCondition wrapped = StatusConditions.Wrapped.create(floor, target, usedMove, floor.random);
				wrapped.addFlag("wrapper:" + usedMove.user.id());

				AppliedStatusCondition wrapping = StatusConditions.Wrapping.create(floor, usedMove.user, usedMove,
						floor.random);
				wrapped.addFlag("wrapped:" + target.id());

				effects.createEffect(new StatusConditionCreatedEvent(floor, wrapped), usedMove, target, floor, missed,
						false, target);
				effects.createEffect(new StatusConditionCreatedEvent(floor, wrapping), usedMove, target, floor, missed,
						false, usedMove.user);
			}
		}
	}

}

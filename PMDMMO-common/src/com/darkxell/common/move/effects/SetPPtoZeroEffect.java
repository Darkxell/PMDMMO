package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonUtils;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.stats.PPChangedEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;

public class SetPPtoZeroEffect extends MoveEffect {

	public SetPPtoZeroEffect(int id) {
		super(id);
	}

	@Override
	protected void mainEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor,
			MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
		super.mainEffects(usedMove, target, flags, floor, calculator, missed, effects);

		if (!missed) {
			LearnedMove move = DungeonUtils.findLastMove(floor, target);
			if (move != null && move.pp() > 0)
				effects.createEffect(new PPChangedEvent(floor, target, -move.pp(), target.moveIndex(move)), usedMove,
						target, floor, missed, false, target);
		}
	}

}

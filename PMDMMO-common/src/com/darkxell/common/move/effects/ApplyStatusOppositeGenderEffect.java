package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.util.language.Message;

public class ApplyStatusOppositeGenderEffect extends ApplyStatusConditionEffect
{

	public ApplyStatusOppositeGenderEffect(int id, StatusCondition status, int probability)
	{
		super(id, status, probability);
	}

	@Override
	protected boolean shouldApply(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor, MoveEffectCalculator calculator, boolean missed,
			MoveEvents effects)
	{
		boolean sup = super.shouldApply(usedMove, target, flags, floor, calculator, missed, effects);
		if (!sup) return false;

		if (target.gender() == Pokemon.GENDERLESS)
		{
			effects.events.add(new MessageEvent(floor, new Message("status.immune.genderless").addReplacement("<pokemon>", target.getNickname())));
			return false;
		}

		if (target.gender() == usedMove.user.gender())
		{
			effects.events.add(new MessageEvent(floor, new Message("status.immune.same_gender").addReplacement("<pokemon>", target.getNickname())
					.addReplacement("<user>", usedMove.user.getNickname())));
			return false;
		}

		return true;
	}

}

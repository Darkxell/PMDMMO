package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.util.RandomUtil;

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
	protected void useOn(MoveUse usedMove, DungeonPokemon target, Floor floor, MoveEffectCalculator calculator, boolean missed, ArrayList<DungeonEvent> events)
	{
		super.useOn(usedMove, target, floor, calculator, missed, events);

		if (!missed && floor.random.nextDouble() * 100 < this.probability && this.status.affects(target)) events.add(new StatusConditionCreatedEvent(floor,
				new AppliedStatusCondition(this.status, target, RandomUtil.nextIntInBounds(this.status.durationMin, this.status.durationMax, floor.random))));
	}

}

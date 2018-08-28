package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;

public class StatChangeEffect extends MoveEffect
{

	public final int stage;
	public final Stat stat;
	public final int probability;

	public StatChangeEffect(int id, Stat stat, int stage, int probability)
	{
		super(id);
		this.stat = stat;
		this.stage = stage;
		this.probability = probability;
	}

	@Override
	public void additionalEffects(MoveUse usedMove, DungeonPokemon target, Floor floor, MoveEffectCalculator calculator, boolean missed, MoveEvents effects)
	{
		super.additionalEffects(usedMove, target, floor, calculator, missed, effects);

		if (!missed && floor.random.nextDouble() * 100 < this.probability) effects.createEffect(new StatChangedEvent(floor, target, this.stat, this.stage),
				usedMove, target, floor, missed, usedMove.move.move().dealsDamage, target);
	}

}

package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;

public class StatChangeEffect extends MoveEffect
{

	public final int stage;
	public final Stat stat;

	public StatChangeEffect(int id, Stat stat, int stage)
	{
		super(id);
		this.stat = stat;
		this.stage = stage;
	}

	@Override
	protected void moveEffects(MoveUse usedMove, DungeonPokemon target, Floor floor, MoveEffectCalculator calculator, boolean missed)
	{
		super.moveEffects(usedMove, target, floor, calculator, missed);

		if (!missed) this.createEffect(new StatChangedEvent(floor, target, this.stat, this.stage), usedMove, target, floor, missed,
				usedMove.move.move().dealsDamage, target);
	}

}

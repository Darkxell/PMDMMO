package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;

public class DrainEffect extends MoveEffect
{

	public final int percent;

	public DrainEffect(int id, int percent)
	{
		super(id);
		this.percent = percent;
	}

	@Override
	protected void moveEffects(MoveUse usedMove, DungeonPokemon target, Floor floor, MoveEffectCalculator calculator, boolean missed)
	{
		super.moveEffects(usedMove, target, floor, calculator, missed);
		if (!missed)
		{
			DamageDealtEvent damage = null;
			for (DungeonEvent e : this.currentEffects())
				if (e instanceof DamageDealtEvent)
				{
					damage = (DamageDealtEvent) e;
					break;
				}
			if (damage != null) this.createEffect(new HealthRestoredEvent(floor, usedMove.user, damage.damage * this.percent / 100), usedMove, target, floor,
					missed, true, usedMove.user);
		}
	}

}

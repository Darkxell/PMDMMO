package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.pokemon.DungeonPokemon;

public class RecoilEffect extends MoveEffect
{

	public final double percentage;

	public RecoilEffect(int id, double percentage)
	{
		super(id);
		this.percentage = percentage;
	}

	@Override
	protected void moveEffects(MoveUse usedMove, DungeonPokemon target, Floor floor, MoveEffectCalculator calculator, boolean missed)
	{
		super.moveEffects(usedMove, target, floor, calculator, missed);
		if (!missed)
		{
			int damage = -1;
			for (DungeonEvent e : this.currentEffects())
				if (e instanceof DamageDealtEvent)
				{
					DamageDealtEvent d = (DamageDealtEvent) e;
					if (d.target == target && d.source == usedMove) damage = d.damage;
				}
			damage *= this.percentage / 100;
			this.createEffect(new DamageDealtEvent(floor, usedMove.user, usedMove, damage), usedMove, target, floor, missed, true, usedMove.user);
		}
	}

}

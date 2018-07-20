package com.darkxell.common.move.effects;

import java.util.ArrayList;

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
	protected void useOn(MoveUse usedMove, DungeonPokemon target, Floor floor, MoveEffectCalculator calculator, boolean missed, ArrayList<DungeonEvent> events)
	{
		super.useOn(usedMove, target, floor, calculator, missed, events);
		if (!missed)
		{
			DamageDealtEvent damage = null;
			for (DungeonEvent e : events)
				if (e instanceof DamageDealtEvent)
				{
					damage = (DamageDealtEvent) e;
					break;
				}
			if (damage != null) events.add(new HealthRestoredEvent(floor, usedMove.user, damage.damage * this.percent / 100));
		}
	}

}

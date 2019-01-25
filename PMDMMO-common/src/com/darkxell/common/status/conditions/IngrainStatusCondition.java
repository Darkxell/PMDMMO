package com.darkxell.common.status.conditions;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;

public class IngrainStatusCondition extends PeriodicHealingStatusCondition {

	public IngrainStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, int heal, int period) {
		super(id, isAilment, durationMin, durationMax, heal, period);
	}

	@Override
	public boolean preventsMoving(DungeonPokemon pokemon, Floor floor) {
		return true;
	}

}

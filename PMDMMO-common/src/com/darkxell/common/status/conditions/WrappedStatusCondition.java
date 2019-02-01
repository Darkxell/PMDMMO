package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;

public class WrappedStatusCondition extends PeriodicDamageStatusCondition {

	public WrappedStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, int period, int damage) {
		super(id, isAilment, durationMin, durationMax, period, damage);
	}

	@Override
	public void onEnd(Floor floor, AppliedStatusCondition instance, StatusConditionEndReason reason,
			ArrayList<DungeonEvent> events) {
		super.onEnd(floor, instance, reason, events);

		String wrapperid = null;
		for (String flag : instance.listFlags())
			if (flag.startsWith("wrapper:")) wrapperid = flag.substring("wrapper:".length());

		if (wrapperid != null && wrapperid.matches("-?\\d+")) {
			long id = Integer.parseInt(wrapperid);
			DungeonPokemon wrapper = floor.findPokemon(id);
			if (wrapper != null && wrapper.hasStatusCondition(StatusConditions.Wrapping))
				wrapper.getStatusCondition(StatusConditions.Wrapping).finish(floor, reason, events);
		}
	}

}

package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.AppliedStatusCondition;

public class AbilityClearStatusAilmentOnTurnEnd extends Ability {

	public final int chance;

	public AbilityClearStatusAilmentOnTurnEnd(int id, int chance) {
		super(id);
		this.chance = chance;
	}

	@Override
	public void onTurnStart(Floor floor, DungeonPokemon pokemon, ArrayList<DungeonEvent> events) {
		super.onTurnStart(floor, pokemon, events);

		if (pokemon.hasStatusCondition(null)) {
			boolean triggers = floor.random.nextDouble() * 100 < this.chance;
			if (triggers) {
				events.add(new TriggeredAbilityEvent(floor, pokemon));
				for (AppliedStatusCondition condition : pokemon.activeStatusConditions())
					events.add(new StatusConditionEndedEvent(floor, condition));
			}
		}

	}

}

package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public class AbilityPreventsAnyStatLossFromOthers extends Ability
{

	public AbilityPreventsAnyStatLossFromOthers(int id)
	{
		super(id);
	}

	@Override
	public void onPreEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned, ArrayList<DungeonEvent> resultingEvents)
	{
		super.onPreEvent(floor, event, concerned, resultingEvents);
		if (event instanceof StatChangedEvent)
		{
			StatChangedEvent e = (StatChangedEvent) event;
			if (e.stage < 0)
			{
				if (e.source instanceof MoveUse)
				{
					MoveUse u = (MoveUse) e.source;
					if (u.user != concerned)
					{
						event.consume();
						resultingEvents.add(new TriggeredAbilityEvent(floor, concerned));
					}
				}
			}
		}
	}

}

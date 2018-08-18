package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.weather.Weather;

public class AbilityDoubleAttacks extends Ability
{

	/** Weathers that activate this Ability. */
	private Weather[] activators;

	public AbilityDoubleAttacks(int id, Weather... activators)
	{
		super(id);
		this.activators = activators;
	}

	@Override
	public void onEvent(Floor floor, DungeonEvent event, ArrayList<DungeonEvent> resultingEvents)
	{
		super.onEvent(floor, event, resultingEvents);
		if (event instanceof MoveSelectionEvent && !event.hasFlag("isdoubled"))
		{
			MoveSelectionEvent e = (MoveSelectionEvent) event;
			boolean active = e.usedMove().user.ability() == this;
			Weather current = floor.currentWeather().weather;

			if (active) for (Weather a : this.activators)
				if (current == a)
				{
					active = true;
					break;
				}

			if (active)
			{
				MoveSelectionEvent n = new MoveSelectionEvent(e.floor, e.usedMove().move, e.usedMove().user, e.usedMove().direction);
				e.addFlag("isdoubled");
				n.addFlag("isdoubled");
				n.setConsumesNoPP();
				n.setPriority(DungeonEvent.PRIORITY_ACTION_END);
				resultingEvents.add(new TriggeredAbilityEvent(floor, e.usedMove().user).setPriority(DungeonEvent.PRIORITY_ACTION_END));
				resultingEvents.add(n);
			}
		}
	}

}

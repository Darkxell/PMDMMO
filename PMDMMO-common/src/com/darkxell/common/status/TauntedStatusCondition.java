package com.darkxell.common.status;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class TauntedStatusCondition extends StatusCondition
{

	public TauntedStatusCondition(int id, int durationMin, int durationMax)
	{
		super(id, durationMin, durationMax);
	}

	@Override
	public void onPreEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned, ArrayList<DungeonEvent> resultingEvents)
	{
		if (event instanceof MoveSelectionEvent && concerned.hasStatusCondition(this) && ((MoveSelectionEvent) event).usedMove().user == concerned
				&& !((MoveSelectionEvent) event).usedMove().move.move().dealsDamage)
		{
			event.consume();
			resultingEvents.add(new MessageEvent(floor, new Message("status.trigger.27").addReplacement("<pokemon>", concerned.getNickname())));
		}
		super.onPreEvent(floor, event, concerned, resultingEvents);
	}

}

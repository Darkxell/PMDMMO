package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.pokemon.DungeonPokemon;

public abstract class AbilityOnHit extends Ability
{

	public final int probability;

	public AbilityOnHit(int id, int probability)
	{
		super(id);
		this.probability = probability;
	}

	@Override
	public void onPostEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned, ArrayList<DungeonEvent> resultingEvents)
	{
		if (event instanceof DamageDealtEvent)
		{
			DamageDealtEvent d = (DamageDealtEvent) event;
			if (d.target.ability() == this && d.target == concerned && d.source instanceof MoveUse
					&& ((MoveUse) d.source).move.move().category == MoveCategory.Physical && floor.random.nextDouble() * 100 < this.probability)
			{
				resultingEvents.add(new TriggeredAbilityEvent(floor, d.target));
				this.onHit(floor, d, (MoveUse) d.source, resultingEvents);
			}
		}
	}

	/** Called when a Pokemon uses a damaging, physical move on a Pokemon with this ability, and this abtility triggers.
	 * 
	 * @param floor - The Floor context.
	 * @param event - The triggering Event.
	 * @param source - The Move that was used.
	 * @param resultingEvents - List of Event to add resulting events to. */
	protected abstract void onHit(Floor floor, DamageDealtEvent event, MoveUse source, ArrayList<DungeonEvent> resultingEvents);

}

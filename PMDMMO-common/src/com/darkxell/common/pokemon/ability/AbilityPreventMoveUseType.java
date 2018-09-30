package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;

public class AbilityPreventMoveUseType extends Ability
{

	public final PokemonType type;

	public AbilityPreventMoveUseType(int id, PokemonType type)
	{
		super(id);
		this.type = type;
	}

	@Override
	public void onPreEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned, ArrayList<DungeonEvent> resultingEvents)
	{
		super.onPreEvent(floor, event, concerned, resultingEvents);
		if (event instanceof MoveUseEvent)
		{
			MoveUseEvent e = (MoveUseEvent) event;
			if (e.usedMove.move.move().type == this.type)
			{
				boolean shouldPrevent = false;
				DungeonPokemon target = e.target;
				DungeonPokemon self = concerned;
				if (target == null) target = e.usedMove.user;
				if (self.tile().isInRoom()) shouldPrevent = floor.roomAt(self.tile()).contains(target.tile());
				else
				{
					shouldPrevent = Math.abs(target.tile().x - self.tile().x) <= floor.data.visionDistance();
					shouldPrevent &= Math.abs(target.tile().y - self.tile().y) <= floor.data.visionDistance();
				}
				shouldPrevent &= !e.usedMove.user.isAlliedWith(self);

				if (shouldPrevent)
				{
					e.consume();
					resultingEvents.add(new TriggeredAbilityEvent(floor, self));
				}
			}
		}
	}

}

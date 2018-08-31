package com.darkxell.common.status;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;

public class BoostMoveType extends StatusCondition
{

	public final PokemonType type;

	public BoostMoveType(int id, int durationMin, int durationMax, PokemonType type)
	{
		super(id, durationMin, durationMax);
		this.type = type;
	}

	@Override
	public double damageMultiplier(MoveUse move, DungeonPokemon target, boolean isUser, Floor floor, ArrayList<DungeonEvent> events)
	{
		if (isUser && move.move.move().type == this.type) return 2;
		return super.damageMultiplier(move, target, isUser, floor, events);
	}

}

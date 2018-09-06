package com.darkxell.common.status.floor;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.FloorStatus;

public class MultiplyTypeFloorStatus extends FloorStatus
{

	public final double multiplier;
	public final PokemonType type;

	public MultiplyTypeFloorStatus(int id, int durationMin, int durationMax, PokemonType type, double multiplier)
	{
		super(id, durationMin, durationMax);
		this.type = type;
		this.multiplier = multiplier;
	}

	@Override
	public double damageMultiplier(MoveUse move, DungeonPokemon target, boolean isUser, Floor floor, String[] flags, ArrayList<DungeonEvent> events)
	{
		if (move.move.move().type == this.type) return this.multiplier;
		return super.damageMultiplier(move, target, isUser, floor, flags, events);
	}

}

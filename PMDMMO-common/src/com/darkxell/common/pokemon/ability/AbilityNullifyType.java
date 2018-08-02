package com.darkxell.common.pokemon.ability;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;

public class AbilityNullifyType extends Ability
{

	public final PokemonType type;

	public AbilityNullifyType(int id, PokemonType type)
	{
		super(id);
		this.type = type;
	}

	@Override
	public double applyEffectivenessModifications(double effectiveness, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor)
	{
		if (!isUser && move.move.move().type == this.type) return PokemonType.NO_EFFECT;
		return super.applyEffectivenessModifications(effectiveness, move, target, isUser, floor);
	}

}

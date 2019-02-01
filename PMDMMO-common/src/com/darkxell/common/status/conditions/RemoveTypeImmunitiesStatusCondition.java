package com.darkxell.common.status.conditions;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.language.Message;

public class RemoveTypeImmunitiesStatusCondition extends StatusCondition
{

	public final PokemonType type;

	public RemoveTypeImmunitiesStatusCondition(int id, boolean isAilment, int durationMin, int durationMax, PokemonType type)
	{
		super(id, isAilment, durationMin, durationMax);
		this.type = type;
	}

	@Override
	public Pair<Boolean, Message> affects(Floor floor, AppliedStatusCondition condition, DungeonPokemon pokemon)
	{
		if (!pokemon.species().isType(this.type)) return new Pair<Boolean, Message>(false,
				new Message("status.immune.isnttype").addReplacement("<pokemon>", pokemon.getNickname()).addReplacement("<type>", this.type.getName()));
		return super.affects(floor, condition, pokemon);
	}

	@Override
	public double applyEffectivenessModifications(double effectiveness, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor)
	{
		if (target.hasStatusCondition(this) && effectiveness == PokemonType.NO_EFFECT)
		{
			PokemonType other = null;
			if (target.species().type1 != this.type) other = target.species().type1;
			else if (target.species().type2 != this.type) other = target.species().type2;

			if (other != null) effectiveness = move.move.move().type.effectivenessOn(other);
			else effectiveness = PokemonType.NORMALLY_EFFECTIVE;
		}
		return super.applyEffectivenessModifications(effectiveness, move, target, isUser, floor);
	}

}

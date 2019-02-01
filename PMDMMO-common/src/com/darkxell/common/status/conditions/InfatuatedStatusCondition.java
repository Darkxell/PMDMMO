package com.darkxell.common.status.conditions;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.language.Message;

public class InfatuatedStatusCondition extends PreventsMovesStatusCondition
{

	public InfatuatedStatusCondition(int id, boolean isAilment, int durationMin, int durationMax)
	{
		super(id, isAilment, durationMin, durationMax);
	}

	@Override
	public Pair<Boolean, Message> affects(Floor floor, AppliedStatusCondition condition, DungeonPokemon pokemon)
	{
		if (pokemon.gender() == Pokemon.GENDERLESS)
		{
			Message m = new Message("status.immune.genderless").addReplacement("<pokemon>", pokemon.getNickname());
			return new Pair<Boolean, Message>(false, m);
		}

		DungeonPokemon source = null;
		if (condition.source instanceof DungeonPokemon) source = (DungeonPokemon) condition.source;
		if (condition.source instanceof MoveUse) source = ((MoveUse) condition.source).user;

		if (source != null && source.gender() == pokemon.gender())
		{
			Message m = new Message("status.immune.same_gender").addReplacement("<pokemon>", pokemon.getNickname()).addReplacement("<user>",
					source.getNickname());
			return new Pair<Boolean, Message>(false, m);
		}
		return super.affects(floor, condition, pokemon);
	}

}

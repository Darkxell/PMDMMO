package com.darkxell.common.event.move;

import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public class MoveResultEvent
{

	public static enum MoveResult
	{
		DAMAGE_DEALT,
		HEAL,
		MISS;
	}

	/** The damage that was dealt, or health that was healed. See {@link MoveResultEvent#result} for the result of the Move. */
	public final int health;
	/** The Pokémon the Move was used on. */
	public final DungeonPokemon pokemon;
	/** How the Move affected the Target. */
	public final MoveResult result;

	public MoveResultEvent(DungeonPokemon pokemon, MoveResult result, int healthChange)
	{
		this.pokemon = pokemon;
		this.result = result;
		this.health = healthChange;
	}

	public Message resultMessage()
	{
		Message target = this.pokemon.pokemon.getNickname();
		if (this.result == MoveResult.MISS) return new Message("move.miss").addReplacement("<pokemon>", target);
		if (this.result == MoveResult.HEAL) return new Message("move.heal").addReplacement("<pokemon>", target).addReplacement("<amount>",
				Integer.toString(this.health));
		if (this.health == 0) return new Message("move.no_damage").addReplacement("<pokemon>", target);
		return new Message("move.damage_dealt").addReplacement("<pokemon>", target).addReplacement("<amount>", Integer.toString(this.health));
	}

}

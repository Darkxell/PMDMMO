package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.pokemon.ability.AbilityTypeBoost;
import com.darkxell.common.weather.Weather;

public class DealDamageEffect extends MoveEffect
{

	public DealDamageEffect(int id)
	{
		super(id);
	}

	/** @param move - The Move being used.
	 * @param user - The Pokémon using the move.
	 * @param target - The Pokémon receiving the move.
	 * @param floor - The floor context.
	 * @param events - The list of Events created by this Move.
	 * @return The damage dealt by this move. */
	public int damageDealt(Move move, DungeonPokemon user, DungeonPokemon target, Floor floor, ArrayList<DungeonEvent> events)
	{
		int atk = move.category == MoveCategory.Physical ? user.stats.getAttack() : user.stats.getSpecialAttack() + move.power;
		int level = user.level();
		int def = move.category == MoveCategory.Physical ? target.stats.getDefense() : target.stats.getSpecialDefense();
		float constant = ((atk - def) * 1f / 8) + (level * 2 / 3);

		// Damage modification
		float d = (((constant * 2) - def) + 10) + ((constant * constant) / 20);
		if (d < 1) d = 1;
		else if (d > 999) d = 999;

		// Abilities
		if (user.ability() instanceof AbilityTypeBoost && user.getHp() <= Math.floor(user.getMaxHP() / 4)
				&& move.type == ((AbilityTypeBoost) user.ability()).type)
		{
			events.add(new TriggeredAbilityEvent(floor, user));
			d *= 2;
		}

		// Weather
		{
			Weather w = floor.currentWeather().weather;
			if (w == Weather.SUNNY)
			{
				if (move.type == PokemonType.Fire) d *= 1.5;
				else if (move.type == PokemonType.Water) d *= 0.5;
			} else if (w == Weather.RAIN)
			{
				if (move.type == PokemonType.Fire) d *= 0.5;
				else if (move.type == PokemonType.Water) d *= 1.5;
			} else if (w == Weather.CLOUDS && move.type != PokemonType.Normal) d *= 0.75;
			else if (w == Weather.FOG && move.type == PokemonType.Electric) d *= 0.5;
		}

		// Critical hit ?
		boolean crit = false;
		{
			double c = 0.12;
			if (floor.random.nextDouble() < c) crit = true;
		}
		if (crit) d *= 1.5;

		// Damage multiplier
		{
			float multiplier = move.type == null ? 1 : move.type.effectivenessOn(target.species());
			if (user.species().type1 == move.type || user.species().type2 == move.type) multiplier *= 1.5;

			d *= multiplier;
		}

		// Damage randomness
		d *= (57344 + Math.floor(floor.random.nextDouble() * 16384)) / 65536;

		return (int) d;
	}

	@Override
	public void useOn(MoveUse usedMove, DungeonPokemon target, Floor floor, boolean missed, ArrayList<DungeonEvent> events)
	{
		events.add(new DamageDealtEvent(floor, target, usedMove, missed ? 0 : this.damageDealt(usedMove.move.move(), usedMove.user, target, floor, events)));
	}

}

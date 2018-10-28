package com.darkxell.client.mechanics.animation.movement;

import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;
import com.darkxell.common.pokemon.DungeonPokemon;

public class LongTackleAnimationMovement extends TackleAnimationMovement
{

	public LongTackleAnimationMovement(PokemonAnimation animation, DungeonPokemon pokemon)
	{
		super(animation, pokemon);
	}

	@Override
	protected TravelAnimation createTravel()
	{
		return new TravelAnimation(this.location.location(), this.location.adjacentTile(this.pokemon.facing()).adjacentTile(this.pokemon.facing()).location());
	}

}

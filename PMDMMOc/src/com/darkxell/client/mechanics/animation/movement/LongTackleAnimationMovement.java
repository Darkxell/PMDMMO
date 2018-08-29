package com.darkxell.client.mechanics.animation.movement;

import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.TravelAnimation;

public class LongTackleAnimationMovement extends TackleAnimationMovement
{

	public LongTackleAnimationMovement(PokemonAnimation animation)
	{
		super(animation);
	}

	@Override
	protected TravelAnimation createTravel()
	{
		return new TravelAnimation(this.location.location(), this.location.adjacentTile(this.pokemon.facing()).adjacentTile(this.pokemon.facing()).location());
	}

}

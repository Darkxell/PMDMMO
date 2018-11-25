package com.darkxell.client.mechanics.animation.movement;

import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;

public class LongTackleAnimationMovement extends TackleAnimationMovement
{

	public LongTackleAnimationMovement(PokemonAnimation animation)
	{
		super(animation);
	}

	@Override
	protected TravelAnimation createTravel()
	{
		return new TravelAnimation(this.location, this.renderer.sprite().getFacingDirection().move(this.location, 2));
	}

}

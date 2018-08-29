package com.darkxell.client.mechanics.animation.movement;

import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.TravelAnimation;
import com.darkxell.common.dungeon.floor.Tile;

public class TackleAnimationMovement extends PokemonAnimationMovement
{
	public static final int TOTAL = 30, CHARGE = TOTAL / 3, MOVEMENT = TOTAL / 6;

	protected final Tile location;
	protected TravelAnimation travel;

	public TackleAnimationMovement(PokemonAnimation animation)
	{
		super(animation, TOTAL);
		this.location = this.pokemon.tile();
		this.travel = this.createTravel();
	}

	protected TravelAnimation createTravel()
	{
		return new TravelAnimation(this.location.location(), this.location.adjacentTile(this.pokemon.facing()).location());
	}

	@Override
	public void onFinish()
	{
		super.onFinish();
		this.renderer.setXY(this.location.x, this.location.y);
	}

	@Override
	public void update()
	{
		float completion = this.tick() * 1f;
		if (this.tick() >= CHARGE && this.tick() <= CHARGE + MOVEMENT) completion -= CHARGE;
		else if (this.tick() >= CHARGE * 2 + MOVEMENT && this.tick() <= TOTAL) completion = 30 - completion;
		else completion = -1;

		if (completion != -1 && !this.isOver())
		{
			this.travel.update(completion * 0.75f / MOVEMENT);
			this.renderer.setXY(this.travel.current().getX(), this.travel.current().getY());
		}
	}
}

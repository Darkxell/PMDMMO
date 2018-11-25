package com.darkxell.client.mechanics.animation.movement;

import java.awt.geom.Point2D;

import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;

public class TackleAnimationMovement extends PokemonAnimationMovement
{
	public static final int TOTAL = 30, CHARGE = TOTAL / 3, MOVEMENT = TOTAL / 6;

	protected final Point2D location;
	protected TravelAnimation travel;

	public TackleAnimationMovement(PokemonAnimation animation)
	{
		super(animation, TOTAL);
		this.location = new Point2D.Double(this.renderer.baseX(), this.renderer.baseY());
		this.travel = this.createTravel();
	}

	protected TravelAnimation createTravel()
	{
		return new TravelAnimation(this.location, this.renderer.sprite().getFacingDirection().move(this.location));
	}

	@Override
	public void onFinish()
	{
		super.onFinish();
		if (this.renderer != null) this.renderer.setXY(this.location.getX(), this.location.getY());
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

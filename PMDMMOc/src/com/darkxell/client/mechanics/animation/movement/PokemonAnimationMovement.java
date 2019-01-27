package com.darkxell.client.mechanics.animation.movement;

import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.graphics.renderer.AbstractPokemonRenderer;
import com.darkxell.common.util.Logger;

public abstract class PokemonAnimationMovement {

	public static PokemonAnimationMovement create(PokemonAnimation animation, String movementID) {
		switch (movementID) {
			case "2tiles":
				return new LongTackleAnimationMovement(animation);

			case "smalljump":
				return new SmallJumpAnimationMovement(animation);

			default:
				Logger.w("Unknown Pokemon animation movement ID: " + movementID);
				return null;
		}
	}

	public final int duration;
	public final PokemonAnimation parentAnimation;
	public final AbstractPokemonRenderer renderer;

	public PokemonAnimationMovement(PokemonAnimation animation, int duration) {
		this.parentAnimation = animation;
		this.renderer = this.parentAnimation.renderer;
		this.duration = duration;
	}

	public boolean isOver() {
		return this.parentAnimation.isOver();
	}

	public void onFinish() {}

	public void start() {}

	public int tick() {
		return this.parentAnimation.tick();
	}

	public abstract void update();

}

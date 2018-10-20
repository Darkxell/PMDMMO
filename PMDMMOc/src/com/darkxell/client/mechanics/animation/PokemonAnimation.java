package com.darkxell.client.mechanics.animation;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.animation.movement.PokemonAnimationMovement;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.pokemon.Pokemon;

/** An animation that is displayed on a Pokemon. */
public class PokemonAnimation extends AbstractAnimation
{

	/** Describes the movement of the Pokemon during this Animation. May be null if the Pokemon doesn't move. */
	PokemonAnimationMovement movement;
	/** A reference to the Pokemon's renderer. */
	public final AbstractPokemonRenderer renderer;
	/** The state to give to the Pokemon. null if shouldn't be changed. */
	PokemonSpriteState state;
	/** The number of ticks to wait before setting the state. */
	int stateDelay = 0;
	/** The Pokemon to draw. */
	public final Pokemon target;
	/** Coordinates of the center of the Pokemon. */
	protected double x, y;

	public PokemonAnimation(Pokemon target, AbstractPokemonRenderer renderer, int duration, AnimationEndListener listener)
	{
		super(duration, listener);
		this.target = target;
		this.renderer = renderer;
	}

	@Override
	public void onFinish()
	{
		if (this.movement != null) this.movement.onFinish();
		if (this.renderer != null) this.renderer.removeAnimation(this);
		super.onFinish();
	}

	/** Rendering done after the Pokemon is drawn. */
	public void postrender(Graphics2D g, int width, int height)
	{}

	/** Rendering done before the Pokemon is drawn. */
	public void prerender(Graphics2D g, int width, int height)
	{}

	@Override
	@Deprecated
	public void render(Graphics2D g, int width, int height)
	{
		if (this.target == null) this.postrender(g, width, height);
	}

	@Override
	public void start()
	{
		super.start();
		if (this.renderer != null) this.renderer.addAnimation(this);
		if (this.state != null && this.stateDelay == 0 && this.renderer != null) this.renderer.sprite().setState(this.state);
		if (this.movement != null) this.movement.start();
	}

	@Override
	public void update()
	{
		super.update();
		if (this.state != null && this.tick() == this.stateDelay) this.renderer.sprite().setState(this.state);
		if (this.renderer != null)
		{
			this.x = this.renderer.drawX();
			this.y = this.renderer.drawY();
		}
		if (this.movement != null) this.movement.update();
	}

}

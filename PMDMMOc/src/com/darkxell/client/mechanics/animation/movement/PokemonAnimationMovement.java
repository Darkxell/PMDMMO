package com.darkxell.client.mechanics.animation.movement;

import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.common.pokemon.DungeonPokemon;

public abstract class PokemonAnimationMovement
{

	public final int duration;
	public final PokemonAnimation parentAnimation;
	public final DungeonPokemon pokemon;
	public final AbstractPokemonRenderer renderer;

	public PokemonAnimationMovement(PokemonAnimation animation, int duration)
	{
		this.parentAnimation = animation;
		this.pokemon = this.parentAnimation.target;
		this.renderer = this.parentAnimation.renderer;
		this.duration = duration;
	}

	public boolean isOver()
	{
		return this.parentAnimation.isOver();
	}

	public void onFinish()
	{}

	public void start()
	{}

	public int tick()
	{
		return this.parentAnimation.tick();
	}

	public abstract void update();

}

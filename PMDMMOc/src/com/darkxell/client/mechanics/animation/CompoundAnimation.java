package com.darkxell.client.mechanics.animation;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.common.pokemon.DungeonPokemon;

public class CompoundAnimation extends PokemonAnimation
{

	private ArrayList<AbstractAnimation> components = new ArrayList<>();

	public CompoundAnimation(DungeonPokemon target, AnimationEndListener listener)
	{
		super(target, 0, listener);
	}

	public CompoundAnimation add(AbstractAnimation a)
	{
		if (this.components.contains(a)) return this;
		this.components.add(a);

		this.delayTime = Math.max(this.delayTime, a.delayTime);
		this.duration = Math.max(this.duration, a.duration);

		return this;
	}

	@Override
	public void postrender(Graphics2D g, int width, int height)
	{
		super.postrender(g, width, height);
		for (AbstractAnimation a : this.components)
			a.render(g, width, height); // Keep render because by default, PokemonAnimation#render() calls postrender()
	}

	@Override
	public void prerender(Graphics2D g, int width, int height)
	{
		super.prerender(g, width, height);
		for (AbstractAnimation a : this.components)
			if (a instanceof PokemonAnimation) ((PokemonAnimation) a).prerender(g, width, height);
	}

	@Override
	public void start()
	{
		super.start();
		for (AbstractAnimation a : this.components)
			a.start();
	}

	@Override
	public void stop()
	{
		super.stop();
		for (AbstractAnimation a : this.components)
			a.stop();
	}

	@Override
	public void update()
	{
		super.update();
		for (AbstractAnimation a : this.components)
			a.update();
	}

}

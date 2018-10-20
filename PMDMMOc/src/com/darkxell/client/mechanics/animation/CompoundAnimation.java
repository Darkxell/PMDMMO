package com.darkxell.client.mechanics.animation;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.common.pokemon.Pokemon;

public class CompoundAnimation extends PokemonAnimation
{

	private ArrayList<Integer> componentDelays = new ArrayList<>();
	private ArrayList<AbstractAnimation> components = new ArrayList<>();

	public CompoundAnimation(Pokemon target, AbstractPokemonRenderer renderer, AnimationEndListener listener)
	{
		super(target, renderer, 0, listener);
	}

	public CompoundAnimation add(AbstractAnimation a, Integer delay)
	{
		if (this.components.contains(a)) return this;
		this.components.add(a);
		this.componentDelays.add(delay);

		this.delayTime = Math.max(this.delayTime, delay + a.delayTime);
		this.duration = Math.max(this.duration, delay + a.duration);

		return this;
	}

	@Override
	public void postrender(Graphics2D g, int width, int height)
	{
		super.postrender(g, width, height);
		for (int i = 0; i < this.components.size(); ++i)
			if (this.tick() >= this.componentDelays.get(i)) this.components.get(i).render(g, width, height); // Keep render because by default, PokemonAnimation#render() calls postrender()
	}

	@Override
	public void prerender(Graphics2D g, int width, int height)
	{
		super.prerender(g, width, height);
		for (int i = 0; i < this.components.size(); ++i)
			if (this.components.get(i) instanceof PokemonAnimation && this.tick() >= this.componentDelays.get(i))
				((PokemonAnimation) this.components.get(i)).prerender(g, width, height);
	}

	@Override
	public void start()
	{
		super.start();
		for (int i = 0; i < this.components.size(); ++i)
			if (this.componentDelays.get(i) == 0) this.components.get(i).start();
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
		for (int i = 0; i < this.components.size(); ++i)
		{
			if (this.tick() == this.componentDelays.get(i)) this.components.get(i).start();
			else if (this.tick() > this.componentDelays.get(i)) this.components.get(i).update();
		}
	}

}

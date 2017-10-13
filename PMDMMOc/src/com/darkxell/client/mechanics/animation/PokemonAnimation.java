package com.darkxell.client.mechanics.animation;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.floor.PokemonRenderer;
import com.darkxell.common.pokemon.DungeonPokemon;

/** An animation that is displayed on a Pok�mon. */
public class PokemonAnimation extends AbstractAnimation
{

	/** A reference to the Pok�mon's renderer. */
	public final PokemonRenderer renderer;
	/** The Pok�mon to draw. */
	public final DungeonPokemon target;
	/** Coordinates of the center of the Pok�mon. */
	protected double x, y;

	public PokemonAnimation(DungeonPokemon target, int duration, AnimationEndListener listener)
	{
		super(duration, listener);
		this.target = target;
		this.renderer = Persistance.dungeonState.pokemonRenderer.getRenderer(this.target);
		this.renderer.addAnimation(this);
	}

	@Override
	public void onFinish()
	{
		this.renderer.removeAnimation(this);
		super.onFinish();
	}

	/** Rendering done after the Pok�mon is drawn. */
	public void postrender(Graphics2D g, int width, int height)
	{}

	/** Rendering done before the Pok�mon is drawn. */
	public void prerender(Graphics2D g, int width, int height)
	{}

	@Override
	@Deprecated
	public void render(Graphics2D g, int width, int height)
	{}

	@Override
	public void update()
	{
		super.update();
		this.x = this.renderer.x() * TILE_SIZE + TILE_SIZE / 2;
		this.y = this.renderer.y() * TILE_SIZE + TILE_SIZE / 2;
	}

}

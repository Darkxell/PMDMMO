package com.darkxell.client.mechanics.animation;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.movement.PokemonAnimationMovement;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.pokemon.DungeonPokemon;

/** An animation that is displayed on a Pokémon. */
public class PokemonAnimation extends AbstractAnimation
{

	/** Describes the movement of the Pokémon during this Animation. May be null if the Pokémon doesn't move. */
	PokemonAnimationMovement movement;
	/** A reference to the Pokémon's renderer. */
	public final AbstractPokemonRenderer renderer;
	/** The state to give to the Pokémon. null if shouldn't be changed. */
	PokemonSpriteState state;
	/** The Pokémon to draw. */
	public final DungeonPokemon target;
	/** Coordinates of the center of the Pokémon. */
	protected double x, y;

	public PokemonAnimation(DungeonPokemon target, int duration, AnimationEndListener listener)
	{
		super(duration, listener);
		this.target = target;
		this.renderer = this.target == null ? null : Persistance.dungeonState.pokemonRenderer.getRenderer(this.target);
		if (this.renderer != null) this.renderer.addAnimation(this);
	}

	@Override
	public void onFinish()
	{
		if (this.movement != null) this.movement.onFinish();
		if (this.renderer != null) this.renderer.removeAnimation(this);
		super.onFinish();
	}

	/** Rendering done after the Pokémon is drawn. */
	public void postrender(Graphics2D g, int width, int height)
	{}

	/** Rendering done before the Pokémon is drawn. */
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
		if (this.state != null) this.renderer.sprite.setState(this.state);
		if (this.movement != null) this.movement.start();
	}

	@Override
	public void update()
	{
		super.update();
		if (this.renderer != null)
		{
			this.x = this.renderer.x() + TILE_SIZE / 2;
			this.y = this.renderer.y() + TILE_SIZE / 2;
		}
		if (this.movement != null) this.movement.update();
	}

}

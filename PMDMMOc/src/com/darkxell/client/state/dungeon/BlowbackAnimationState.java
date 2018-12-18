package com.darkxell.client.state.dungeon;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.travel.ArcAnimation;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;
import com.darkxell.client.renderers.pokemon.DungeonPokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.event.pokemon.BlowbackPokemonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public class BlowbackAnimationState extends AnimationState
{
	public static final int DURATION_TILE = 6;

	public final BlowbackPokemonEvent event;
	private AnimationEndListener listener;
	public final DungeonPokemon pokemon;
	private DungeonPokemonRenderer renderer;
	private boolean shouldBounce;
	private int tick, duration;
	private TravelAnimation travel;

	public BlowbackAnimationState(DungeonState parent, BlowbackPokemonEvent event, AnimationEndListener listener)
	{
		super(parent);
		this.event = event;
		this.pokemon = event.pokemon;
		this.travel = new TravelAnimation(event.origin, event.destination());
		this.tick = 0;
		this.duration = (int) (this.travel.distance() * DURATION_TILE);
		this.listener = listener;
		this.shouldBounce = event.wasHurt();
	}

	@Override
	public void onEnd()
	{
		super.onEnd();
		this.renderer.sprite().resetToDefaultState();
		this.renderer.sprite().setAnimated(true);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		this.renderer = Persistence.dungeonState.pokemonRenderer.getRenderer(this.pokemon);
		this.renderer.sprite().setFacingDirection(this.event.direction.opposite());
		this.renderer.sprite().setState(PokemonSpriteState.HURT);
		this.renderer.sprite().setAnimated(false);
	}

	@Override
	public void update()
	{
		super.update();
		++this.tick;
		this.travel.update(this.tick * 1. / this.duration);
		this.renderer.setXY(this.travel.current().getX() - .5, this.travel.current().getY() - .5);
		if (this.tick == this.duration)
		{
			if (this.shouldBounce)
			{
				this.tick = 0;
				this.duration = 15;
				this.travel = new ArcAnimation(this.event.destination(), this.event.destination().adjacentTile(this.event.direction.opposite()));
				this.shouldBounce = false;
			} else
			{
				Persistence.dungeonState.setDefaultState();
				this.listener.onAnimationEnd(this.animation);
			}
		}
	}

}

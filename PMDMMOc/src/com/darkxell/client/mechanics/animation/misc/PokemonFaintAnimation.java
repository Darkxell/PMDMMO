package com.darkxell.client.mechanics.animation.misc;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.pokemon.Pokemon;

public class PokemonFaintAnimation extends PokemonAnimation
{
	public static final int DURATION = 30;

	public PokemonFaintAnimation(Pokemon target, AbstractPokemonRenderer renderer, AnimationEndListener listener)
	{
		super(target, renderer, DURATION, listener);
	}

	@Override
	public void onFinish()
	{
		this.renderer.sprite().setDefaultState(PokemonSpriteState.IDLE, true);
		if (this.target != Persistance.player.getTeamLeader()) Persistance.dungeonState.pokemonRenderer.unregister(this.target.getDungeonPokemon());
		super.onFinish();
	}

	@Override
	public void start()
	{
		super.start();
		this.renderer.sprite().setDefaultState(PokemonSpriteState.HURT, true);
	}

	@Override
	public void update()
	{
		super.update();
		this.renderer.setAlpha(1 - this.completion());
	}

}

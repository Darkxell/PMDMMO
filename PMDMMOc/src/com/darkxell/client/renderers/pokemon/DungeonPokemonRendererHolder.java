package com.darkxell.client.renderers.pokemon;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.EntityRendererHolder;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.common.pokemon.DungeonPokemon;

public class DungeonPokemonRendererHolder extends EntityRendererHolder<DungeonPokemon>
{

	@Override
	public DungeonPokemonRenderer getRenderer(DungeonPokemon pokemon)
	{
		return (DungeonPokemonRenderer) super.getRenderer(pokemon);
	}

	/** @return The Sprite of the input Pokémon. */
	public PokemonSprite getSprite(DungeonPokemon entity)
	{
		DungeonPokemonRenderer renderer = this.getRenderer(entity);
		return renderer == null ? null : renderer.sprite;
	}

	public DungeonPokemonRenderer register(DungeonPokemon pokemon)
	{
		return this.register(pokemon, new DungeonPokemonRenderer(pokemon));
	}

	@Override
	public DungeonPokemonRenderer register(DungeonPokemon entity, AbstractRenderer renderer)
	{
		if (!this.renderers.containsKey(entity)) Persistance.dungeonRenderer.addRenderer(renderer);
		return (DungeonPokemonRenderer) super.register(entity, renderer);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	/** Deletes the Renderer of the input Pokémon. */
	public void unregister(DungeonPokemon entity)
	{
		Persistance.dungeonRenderer.removeRenderer(this.getRenderer(entity));
		super.unregister(entity);
	}

}

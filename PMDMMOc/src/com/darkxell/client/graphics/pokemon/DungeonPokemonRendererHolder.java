package com.darkxell.client.renderers.pokemon;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.EntityRendererHolder;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;

public class DungeonPokemonRendererHolder extends EntityRendererHolder<DungeonPokemon>
{

	@Override
	public DungeonPokemonRenderer getRenderer(DungeonPokemon pokemon)
	{
		return (DungeonPokemonRenderer) super.getRenderer(pokemon);
	}

	/** @return The Sprite of the input Pokemon. */
	public PokemonSprite getSprite(DungeonPokemon entity)
	{
		DungeonPokemonRenderer renderer = this.getRenderer(entity);
		return renderer == null ? null : renderer.sprite;
	}

	public DungeonPokemonRenderer register(DungeonPokemon pokemon)
	{
		DungeonPokemonRenderer r = this.register(pokemon, new DungeonPokemonRenderer(pokemon));
		if (pokemon.type.isAlliedWith(DungeonPokemonType.TEAM_MEMBER))
		{
			if (pokemon.player() == null || pokemon.player() == Persistence.player) r.sprite.setShadowColor(PokemonSprite.ALLY_SHADOW);
			else r.sprite.setShadowColor(PokemonSprite.PLAYER_SHADOW);
		} else r.sprite.setShadowColor(PokemonSprite.ENEMY_SHADOW);
		return r;
	}

	@Override
	public DungeonPokemonRenderer register(DungeonPokemon entity, AbstractRenderer renderer)
	{
		if (!this.renderers.containsKey(entity)) Persistence.dungeonRenderer.addRenderer(renderer);
		return (DungeonPokemonRenderer) super.register(entity, renderer);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	/** Deletes the Renderer of the input Pokemon. */
	public void unregister(AbstractPokemonRenderer renderer)
	{
		Persistence.dungeonRenderer.removeRenderer(renderer);
		super.unregister(renderer);
	}

	/** Deletes the Renderer of the input Pokemon. */
	public void unregister(DungeonPokemon entity)
	{
		Persistence.dungeonRenderer.removeRenderer(this.getRenderer(entity));
		super.unregister(entity);
	}

}

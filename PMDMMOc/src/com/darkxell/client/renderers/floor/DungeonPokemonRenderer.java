package com.darkxell.client.renderers.floor;

import java.awt.Graphics2D;
import java.util.HashMap;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.common.pokemon.DungeonPokemon;

public class DungeonPokemonRenderer extends AbstractRenderer
{

	private final HashMap<DungeonPokemon, PokemonRenderer> renderers = new HashMap<DungeonPokemon, PokemonRenderer>();

	public DungeonPokemonRenderer()
	{
		super(0, 0, MasterDungeonRenderer.LAYER_POKEMON);
		for (DungeonPokemon pokemon : Persistance.floor.listPokemon())
			this.register(pokemon);
	}

	public void draw(Graphics2D g, DungeonPokemon pokemon, int width, int height)
	{
		if (!this.renderers.containsKey(pokemon)) this.register(pokemon);
		this.renderers.get(pokemon).render(g, width, height);
	}

	/** @return The Renderer of the input Pokémon. */
	public PokemonRenderer getRenderer(DungeonPokemon pokemon)
	{
		if (this.renderers.containsKey(pokemon)) return this.renderers.get(pokemon);
		return this.register(pokemon);
	}

	/** @return The Sprite of the input Pokémon. */
	public PokemonSprite getSprite(DungeonPokemon pokemon)
	{
		return this.getRenderer(pokemon).sprite;
	}

	/** Creates a Renderer for the input Pokémon. */
	public PokemonRenderer register(DungeonPokemon pokemon)
	{
		PokemonRenderer renderer = new PokemonRenderer(pokemon);
		this.renderers.put(pokemon, renderer);
		Persistance.dungeonRenderer.addRenderer(renderer);
		return this.getRenderer(pokemon);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	/** Creates the Sprite of the input Pokémon. */
	public void unregister(DungeonPokemon pokemon)
	{
		Persistance.dungeonRenderer.removeRenderer(this.getRenderer(pokemon));
		this.renderers.remove(pokemon);
	}

	public void update()
	{
		for (PokemonRenderer renderer : this.renderers.values())
			renderer.sprite.update();
	}

}

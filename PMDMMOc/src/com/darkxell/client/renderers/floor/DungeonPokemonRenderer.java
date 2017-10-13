package com.darkxell.client.renderers.floor;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.util.HashMap;

import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.DungeonRenderer;
import com.darkxell.common.pokemon.DungeonPokemon;

public class DungeonPokemonRenderer extends AbstractRenderer
{

	private HashMap<DungeonPokemon, PokemonRenderer> renderers;

	public DungeonPokemonRenderer()
	{
		this.setZ(DungeonRenderer.LAYER_POKEMON);
		this.renderers = new HashMap<DungeonPokemon, PokemonRenderer>();
	}

	public void draw(Graphics2D g, DungeonPokemon pokemon, int width, int height)
	{
		if (!this.renderers.containsKey(pokemon)) this.register(pokemon);
		this.renderers.get(pokemon).render(g, width, height);
	}

	/** @return The Renderer of the input Pokémon. */
	public PokemonRenderer getRenderer(DungeonPokemon pokemon)
	{
		return this.renderers.get(pokemon);
	}

	/** Creates a Renderer for the input Pokémon. */
	public PokemonRenderer register(DungeonPokemon pokemon)
	{
		if (!this.renderers.containsKey(pokemon)) this.renderers.put(pokemon, new PokemonRenderer(pokemon));
		return this.getRenderer(pokemon);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		int xStart = this.x() / TILE_SIZE, yStart = this.y() / TILE_SIZE;

		for (DungeonPokemon pokemon : this.renderers.keySet())
			if (this.shouldDraw(pokemon, xStart, yStart, width / TILE_SIZE, height / TILE_SIZE)) this.draw(g, pokemon, width, height);
	}

	private boolean shouldDraw(DungeonPokemon pokemon, int screenX, int screenY, int screenWidth, int screenHeight)
	{
		PokemonRenderer renderer = this.getRenderer(pokemon);
		return renderer.x() >= screenX - 1 && renderer.x() <= screenX + screenWidth + 1 && renderer.y() >= screenY - 1
				&& renderer.y() <= screenY + screenHeight + 1;
	}

	/** Creates the Sprite of the input Pokémon. */
	public void unregister(DungeonPokemon pokemon)
	{
		this.renderers.remove(pokemon);
	}

	public void update()
	{
		for (PokemonRenderer renderer : this.renderers.values())
			renderer.sprite.update();
	}

}

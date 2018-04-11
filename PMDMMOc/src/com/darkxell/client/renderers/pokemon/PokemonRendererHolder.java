package com.darkxell.client.renderers.pokemon;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.HashMap;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;

public class PokemonRendererHolder extends AbstractRenderer
{

	private final HashMap<Pokemon, AbstractPokemonRenderer> renderers = new HashMap<>();

	public PokemonRendererHolder()
	{
		super(0, 0, MasterDungeonRenderer.LAYER_POKEMON);
	}

	public void draw(Graphics2D g, Pokemon pokemon, int width, int height)
	{
		if (this.renderers.containsKey(pokemon)) this.renderers.get(pokemon).render(g, width, height);
	}

	/** @return The Renderer of the input Pokémon. */
	public AbstractPokemonRenderer getRenderer(DungeonPokemon pokemon)
	{
		return this.getRenderer(pokemon.usedPokemon);
	}

	/** @return The Renderer of the input Pokémon. */
	public AbstractPokemonRenderer getRenderer(Pokemon pokemon)
	{
		if (this.renderers.containsKey(pokemon)) return this.renderers.get(pokemon);
		return null;
	}

	/** @return The Sprite of the input Pokémon. */
	public PokemonSprite getSprite(Pokemon pokemon)
	{
		return this.getRenderer(pokemon).sprite;
	}

	public Collection<AbstractPokemonRenderer> listRenderers()
	{
		return this.renderers.values();
	}

	/** Creates a Renderer for the input Pokémon. */
	public AbstractPokemonRenderer register(AbstractPokemonRenderer renderer)
	{
		this.renderers.put(renderer.pokemon, renderer);
		Persistance.dungeonRenderer.addRenderer(renderer);
		return this.getRenderer(renderer.pokemon);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	/** Creates the Sprite of the input Pokémon. */
	public void unregister(Pokemon pokemon)
	{
		Persistance.dungeonRenderer.removeRenderer(this.getRenderer(pokemon));
		this.renderers.remove(pokemon);
	}

	public void update()
	{
		for (AbstractPokemonRenderer renderer : this.renderers.values())
			renderer.sprite.update();
	}

}

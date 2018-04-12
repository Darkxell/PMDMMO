package com.darkxell.client.renderers.pokemon;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.HashMap;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.common.pokemon.DungeonPokemon;

public class PokemonRendererHolder<T> extends AbstractRenderer
{

	private final HashMap<T, AbstractPokemonRenderer<T>> renderers = new HashMap<>();

	public PokemonRendererHolder()
	{
		super(0, 0, MasterDungeonRenderer.LAYER_POKEMON);
	}

	public void draw(Graphics2D g, T pokemon, int width, int height)
	{
		if (this.renderers.containsKey(pokemon)) this.renderers.get(pokemon).render(g, width, height);
	}

	/** @return The Renderer of the input Pokémon. */
	public AbstractPokemonRenderer<T> getRenderer(T pokemon)
	{
		if (this.renderers.containsKey(pokemon)) return this.renderers.get(pokemon);
		return null;
	}

	/** @return The Sprite of the input Pokémon. */
	public PokemonSprite getSprite(T pokemon)
	{
		AbstractPokemonRenderer<T> renderer = this.getRenderer(pokemon);
		return renderer == null ? null : renderer.sprite;
	}

	public Collection<AbstractPokemonRenderer<T>> listRenderers()
	{
		return this.renderers.values();
	}

	/** Registers and returns the input Renderer. */
	public AbstractPokemonRenderer<T> register(AbstractPokemonRenderer<T> renderer)
	{
		this.renderers.put(renderer.pokemon, renderer);
		Persistance.dungeonRenderer.addRenderer(renderer);
		return renderer;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	/** Deletes the Renderer of the input Pokémon. */
	public void unregister(T pokemon)
	{
		if (pokemon instanceof DungeonPokemon) Persistance.dungeonRenderer.removeRenderer(this.getRenderer(pokemon));
		this.renderers.remove(pokemon);
	}

	public void update()
	{
		for (AbstractPokemonRenderer<T> renderer : this.renderers.values())
			renderer.sprite.update();
	}

}

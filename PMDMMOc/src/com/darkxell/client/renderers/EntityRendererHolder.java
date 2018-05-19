package com.darkxell.client.renderers;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;

public class EntityRendererHolder<T> extends AbstractRenderer
{

	private final HashMap<T, AbstractRenderer> renderers = new HashMap<>();

	public EntityRendererHolder()
	{
		super(0, 0, MasterDungeonRenderer.LAYER_POKEMON);
	}

	public void draw(Graphics2D g, T pokemon, int width, int height)
	{
		if (this.renderers.containsKey(pokemon)) this.renderers.get(pokemon).render(g, width, height);
	}

	/** @return The Renderer of the input Pokémon. */
	public AbstractRenderer getRenderer(T pokemon)
	{
		if (this.renderers.containsKey(pokemon)) return this.renderers.get(pokemon);
		return null;
	}

	/** @return The Sprite of the input entity if it's a Pokémon. */
	public PokemonSprite getSprite(T entity)
	{
		AbstractRenderer renderer = this.getRenderer(entity);
		return renderer == null || !(renderer instanceof AbstractPokemonRenderer) ? null : ((AbstractPokemonRenderer) renderer).sprite();
	}

	public ArrayList<AbstractRenderer> listRenderers()
	{
		ArrayList<AbstractRenderer> renderers = new ArrayList<>(this.renderers.values());
		renderers.sort(Comparator.naturalOrder());
		return renderers;
	}

	/** Registers and returns the input Renderer. */
	public AbstractRenderer register(T entity, AbstractRenderer renderer)
	{
		this.renderers.put(entity, renderer);
		return renderer;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	/** Deletes the Renderer of the input Pokémon. */
	public void unregister(T entity)
	{
		this.renderers.remove(entity);
	}

	public void update()
	{
		for (AbstractRenderer renderer : this.renderers.values())
			renderer.update();
	}

}

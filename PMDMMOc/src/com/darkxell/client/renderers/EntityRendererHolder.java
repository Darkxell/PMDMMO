package com.darkxell.client.renderers;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;

/** Maps objects to their renderers for complex states to render.
 *
 * @param <T> - The type of object to render. */
public class EntityRendererHolder<T> extends AbstractRenderer
{

	/** True if the sorted list should be updated when called. Security in case registered() is called between update() and listRenderers(). */
	protected boolean forceReload = false;
	/** Maps objects to their renderers. */
	protected final HashMap<T, AbstractRenderer> renderers = new HashMap<>();
	/** Contains the sorted list of renderers by drawing order. */
	private final ArrayList<AbstractRenderer> sortedRenderers = new ArrayList<>();

	public EntityRendererHolder()
	{
		super(0, 0, MasterDungeonRenderer.LAYER_POKEMON);
	}

	/** @return The Renderer of the input object. If that object is not registered, returns null. */
	public AbstractRenderer getRenderer(T object)
	{
		if (this.renderers.containsKey(object)) return this.renderers.get(object);
		return null;
	}

	/** @return The Sprite of the input object if it's a Pokemon. */
	public PokemonSprite getSprite(T pokemon)
	{
		AbstractRenderer renderer = this.getRenderer(pokemon);
		return renderer == null || !(renderer instanceof AbstractPokemonRenderer) ? null : ((AbstractPokemonRenderer) renderer).sprite();
	}

	/** @return The list of all renderers to call on render, in drawing order. */
	@SuppressWarnings("unchecked")
	public ArrayList<AbstractRenderer> listRenderers()
	{
		if (this.forceReload)
		{
			this.sortedRenderers.sort(Comparator.naturalOrder());
			this.forceReload = false;
		}
		return (ArrayList<AbstractRenderer>) this.sortedRenderers.clone();
	}

	/** Registers and returns the input Renderer. */
	public AbstractRenderer register(T object, AbstractRenderer renderer)
	{
		if (this.renderers.containsKey(object)) return this.getRenderer(object);
		this.renderers.put(object, renderer);
		this.sortedRenderers.add(renderer);
		this.forceReload = true;
		return renderer;
	}

	@Deprecated
	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	/** Deletes the Renderer of the input object. */
	public void unregister(T object)
	{
		if (!this.renderers.containsKey(object)) return;
		this.sortedRenderers.remove(this.renderers.get(object));
		this.renderers.remove(object);
	}

	public void update()
	{
		for (AbstractRenderer renderer : this.renderers.values())
			renderer.update();
		this.sortedRenderers.sort(Comparator.naturalOrder());
		this.forceReload = false;
	}

}

package com.darkxell.client.renderers.pokemon;

import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;

/** Renders a Pokemon. This Renderer's Coordinates' units are Tiles. */
public class FreezonePokemonRenderer extends AbstractPokemonRenderer
{

	public final FreezoneEntity entity;

	public FreezonePokemonRenderer(FreezoneEntity entity, PokemonSprite sprite)
	{
		super(sprite);
		this.entity = entity;
	}

	@Override
	public boolean shouldRender(int width, int height)
	{
		return true;
	}

	@Override
	public void update()
	{
		this.setXY(this.entity.posX, this.entity.posY);
		super.update();
	}

	@Override
	public double x()
	{
		return super.x() * 8;
	}

	@Override
	public double y()
	{
		return super.y() * 8;
	}

}

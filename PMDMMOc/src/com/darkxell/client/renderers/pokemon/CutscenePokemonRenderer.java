package com.darkxell.client.renderers.pokemon;

import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;

/** Renders a Pokémon. This Renderer's Coordinates' units are Tiles. */
public class CutscenePokemonRenderer extends AbstractPokemonRenderer
{

	public final CutsceneEntity entity;

	public CutscenePokemonRenderer(CutsceneEntity entity, PokemonSprite sprite)
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
		this.setXY(this.entity.xPos, this.entity.yPos);
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

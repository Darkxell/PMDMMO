package com.darkxell.client.renderers.pokemon;

import com.darkxell.client.mechanics.freezones.entities.PokemonFreezoneEntity;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;

/** Renders a Pokémon. This Renderer's Coordinates' units are Tiles. */
public class FreezonePokemonRenderer extends AbstractPokemonRenderer<PokemonFreezoneEntity>
{

	public FreezonePokemonRenderer(PokemonFreezoneEntity pokemon, PokemonSprite sprite)
	{
		super(pokemon, sprite);
	}

	@Override
	public boolean shouldRender(int width, int height)
	{
		return true;
	}

}

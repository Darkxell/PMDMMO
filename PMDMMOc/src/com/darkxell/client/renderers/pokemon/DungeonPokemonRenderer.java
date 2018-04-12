package com.darkxell.client.renderers.pokemon;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.common.pokemon.DungeonPokemon;

/** Renders a Pokémon. This Renderer's Coordinates' units are Tiles. */
public class DungeonPokemonRenderer extends AbstractPokemonRenderer<DungeonPokemon>
{

	public final DungeonPokemon pokemon;

	public DungeonPokemonRenderer(DungeonPokemon pokemon)
	{
		super(pokemon, new PokemonSprite(PokemonSpritesets.getSpriteset(pokemon.usedPokemon)));
		this.pokemon = pokemon;
		this.setX(pokemon.tile().x);
		this.setY(pokemon.tile().y);
	}

	@Override
	public boolean shouldRender(int width, int height)
	{
		if (!Persistance.dungeonState.floorVisibility.isVisible(this.pokemon)) return false;
		return super.shouldRender(width, height);
	}

	@Override
	public void update()
	{
		super.update();
		if (this.pokemon.facing() != this.sprite.getFacingDirection()) this.sprite.setFacingDirection(this.pokemon.facing());
	}

}

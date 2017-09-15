package com.darkxell.client.renderers;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.util.HashMap;

import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.resources.images.PokemonSpritesets;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.GameUtil;

/** Renders Dungeon Pokémon. */
public class DungeonPokemonRenderer
{

	public static final DungeonPokemonRenderer instance = new DungeonPokemonRenderer();

	private static byte spriteDirection(short facing)
	{
		for (byte i = 0; i < GameUtil.directions().length; ++i)
			if (facing == GameUtil.directions()[i]) return i;
		return 0;
	}

	private HashMap<DungeonPokemon, PokemonSprite> sprites;

	public DungeonPokemonRenderer()
	{
		this.sprites = new HashMap<DungeonPokemon, PokemonSprite>();
	}

	public void draw(Graphics2D g, DungeonPokemon pokemon, double x, double y)
	{
		if (!this.sprites.containsKey(pokemon)) this.register(pokemon);

		PokemonSprite sprite = this.sprites.get(pokemon);
		if (pokemon.stateChanged)
		{
			sprite.setFacingDirection(spriteDirection(pokemon.facing()));
			pokemon.stateChanged = false;
		}

		if (sprite.getCurrentSprite() != null) g.drawImage(sprite.getCurrentSprite(), (int) (x * TILE_SIZE + TILE_SIZE / 2 - sprite.pointer.gravityX), (int) (y
				* TILE_SIZE + TILE_SIZE / 2 - sprite.pointer.gravityY), null);
	}

	/** @return The Sprite of the input Pokémon. */
	public PokemonSprite getSprite(DungeonPokemon pokemon)
	{
		return this.sprites.get(pokemon);
	}

	/** Creates a Sprite for the input Pokémon. */
	public PokemonSprite register(DungeonPokemon pokemon)
	{
		if (!this.sprites.containsKey(pokemon)) this.sprites.put(pokemon, new PokemonSprite(PokemonSpritesets.getSpriteset(pokemon.pokemon.species.id)));
		return this.getSprite(pokemon);
	}

	/** Creates the Sprite of the input Pokémon. */
	public void unregister(DungeonPokemon pokemon)
	{
		this.sprites.remove(pokemon);
	}

	public void update()
	{
		for (PokemonSprite sprite : this.sprites.values())
			sprite.update();
	}

}

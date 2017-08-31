package com.darkxell.client.renderers;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.util.HashMap;

import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.resources.images.PokemonSpritesets;
import com.darkxell.common.pokemon.PokemonD;
import com.darkxell.common.util.GameUtil;

/** Renders Dungeon Pokémon. */
public class PokemonRenderer
{

	public static final PokemonRenderer instance = new PokemonRenderer();

	private static byte spriteDirection(short facing)
	{
		for (byte i = 0; i < GameUtil.directions().length; ++i)
			if (facing == GameUtil.directions()[i]) return i;
		return 0;
	}

	private HashMap<PokemonD, PokemonSprite> sprites;

	public PokemonRenderer()
	{
		this.sprites = new HashMap<PokemonD, PokemonSprite>();
	}

	public void draw(Graphics2D g, PokemonD pokemon, double x, double y)
	{
		if (!this.sprites.containsKey(pokemon)) this.register(pokemon);

		PokemonSprite sprite = this.sprites.get(pokemon);
		if (pokemon.stateChanged)
		{
			sprite.setFacingDirection(spriteDirection(pokemon.facing()));
			pokemon.stateChanged = false;
		}

		g.drawImage(sprite.getCurrentSprite(), (int) (x * TILE_SIZE + TILE_SIZE / 2 - sprite.pointer.gravityX),
				(int) (y * TILE_SIZE + TILE_SIZE / 2 - sprite.pointer.gravityY), null);
	}

	/** @return The Sprite of the input Pokémon. */
	public PokemonSprite getSprite(PokemonD pokemon)
	{
		return this.sprites.get(pokemon);
	}

	/** Creates a Sprite for the input Pokémon. */
	public PokemonSprite register(PokemonD pokemon)
	{
		if (!this.sprites.containsKey(pokemon)) this.sprites.put(pokemon, new PokemonSprite(PokemonSpritesets.getSpriteset(pokemon.pokemon.species.id)));
		return this.getSprite(pokemon);
	}

	/** Creates the Sprite of the input Pokémon. */
	public void unregister(PokemonD pokemon)
	{
		this.sprites.remove(pokemon);
	}

	public void update()
	{
		for (PokemonSprite sprite : this.sprites.values())
			sprite.update();
	}

}

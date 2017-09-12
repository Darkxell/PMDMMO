package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;
import com.darkxell.common.pokemon.Pokemon;

public class PokemonPortrait
{
	private static final int cols = 10, rows = 10;
	public static final int PORTRAIT_SIZE = 40;
	private static final PokemonPortrait portraits0 = new PokemonPortrait("resources/pokemons/portraits/portraits0.png"), portraits1 = new PokemonPortrait(
			"resources/pokemons/portraits/portraits1.png"), portraits2 = new PokemonPortrait("resources/pokemons/portraits/portraits2.png"),
			portraits3 = new PokemonPortrait("resources/pokemons/portraits/portraits3.png");

	/** @return The portrait for the input Pokémon. */
	public static BufferedImage portrait(Pokemon pokemon)
	{
		int set = pokemon.species.id / 100, index = pokemon.species.id % 100 - 1;
		if (index == -1) // ID 100 is on first page but would give set=1, index=-1
		{
			--set;
			++index;
		}

		PokemonPortrait sheet;
		switch (set)
		{
			case 1:
				sheet = portraits1;
				break;

			case 2:
				sheet = portraits2;
				break;

			case 3:
				sheet = portraits3;
				break;

			default:
				sheet = portraits0;
				break;
		}

		return sheet.sprites[index];
	}

	private BufferedImage[] sprites;

	private PokemonPortrait(String path)
	{
		this.sprites = new BufferedImage[cols * rows];
		BufferedImage source = Res.getBase(path);
		int index = 0;
		for (int x = 0; x < cols; ++x)
			for (int y = 0; y < rows; ++y)
			{
				index = y * cols + x;
				this.sprites[index] = Res.createimage(source, x * PORTRAIT_SIZE, y * PORTRAIT_SIZE, PORTRAIT_SIZE, PORTRAIT_SIZE);
				if (this.sprites[index].getRGB(0, 0) == 0) this.sprites[index] = null;
			}
	}

}

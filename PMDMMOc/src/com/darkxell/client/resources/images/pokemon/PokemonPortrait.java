package com.darkxell.client.resources.images.pokemon;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.images.others.Hud;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.PokemonSpecies;

public class PokemonPortrait
{
	private static final HashMap<Integer, Integer> alternateIDs = new HashMap<Integer, Integer>();
	private static final int cols = 10, rows = 10;
	public static final int PORTRAIT_SIZE = 40;
	private static final PokemonPortrait portraits0 = new PokemonPortrait("/pokemons/portraits/portraits0.png"),
			portraits1 = new PokemonPortrait("/pokemons/portraits/portraits1.png"), portraits2 = new PokemonPortrait("/pokemons/portraits/portraits2.png"),
			portraits3 = new PokemonPortrait("/pokemons/portraits/portraits3.png");
	private static final PokemonPortrait portraits0S = new PokemonPortrait("/pokemons/portraits/portraits0s.png"),
			portraits1S = new PokemonPortrait("/pokemons/portraits/portraits1s.png"), portraits2S = new PokemonPortrait("/pokemons/portraits/portraits2s.png"),
			portraits3S = new PokemonPortrait("/pokemons/portraits/portraits3s.png");
	private static final PokemonPortrait portraitsAlternate = new PokemonPortrait("/pokemons/portraits/portraits-alternate.png");
	private static final PokemonPortrait portraitsAlternateS = new PokemonPortrait("/pokemons/portraits/portraits-alternates.png");

	static
	{
		int id = 0;
		for (PokemonSpecies pokemon : PokemonRegistry.list())
			for (PokemonSpecies form : pokemon.forms())
				alternateIDs.put(form.compoundID(), id++);
	}

	/** Draws the portrait of the input pokémon at the input topright location. */
	public static void drawPortrait(Graphics2D g, Pokemon pokemon, int x, int y)
	{
		g.drawImage(PokemonPortrait.portrait(pokemon), x + 4, y + 4, null);
		g.drawImage(Hud.portrait, x, y, null);
	}

	/** @return The portrait for the input Pokémon. */
	public static BufferedImage portrait(Pokemon pokemon)
	{
		PokemonPortrait sheet = null;
		int set = pokemon.species().id / 100, index = pokemon.species().id % 100 - 1;
		if (pokemon.species().formID != 0)
		{
			sheet = pokemon.isShiny() ? portraitsAlternateS : portraitsAlternate;
			index = alternateIDs.get(pokemon.species().compoundID());
		} else
		{
			if (index == -1) // ID 100 is on first page but would give set=1, index=-1
			{
				--set;
				index = 99;
			}

			switch (set)
			{
				case 1:
					sheet = pokemon.isShiny() ? portraits1S : portraits1;
					break;

				case 2:
					sheet = pokemon.isShiny() ? portraits2S : portraits2;
					break;

				case 3:
					sheet = pokemon.isShiny() ? portraits3S : portraits3;
					break;

				default:
					sheet = pokemon.isShiny() ? portraits0S : portraits0;
					break;
			}
		}

		if (sheet == null) return null;
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

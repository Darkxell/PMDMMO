package com.darkxell.client.resources.images.pokemon;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.RegularSpriteSet;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;

public class PokemonPortrait extends RegularSpriteSet
{
	private static final PokemonPortrait alternates = new PokemonPortrait("/pokemons/portraits/portraits-forms.png", 20),
			alternateShinies = new PokemonPortrait("/pokemons/portraits/portraits-forms-shiny.png", 20);
	private static final PokemonPortrait normal = new PokemonPortrait("/pokemons/portraits/portraits.png", 100),
			shinies = new PokemonPortrait("/pokemons/portraits/portraits-shiny.png", 100);
	public static final int PORTRAIT_SIZE = 40;

	/** Draws the portrait of the input Pokemon at the input topright location. */
	public static void drawPortrait(Graphics2D g, Pokemon pokemon, int x, int y)
	{
		drawPortrait(g, pokemon.species(), pokemon.isShiny(), x, y);
	}

	/** Draws the portrait of the input Pokemon at the input topright location. */
	public static void drawPortrait(Graphics2D g, PokemonSpecies pokemon, boolean shiny, int x, int y)
	{
		g.drawImage(PokemonPortrait.portrait(pokemon, shiny), x + 4, y + 4, null);
		g.drawImage(Sprites.Res_Hud.portrait.image(), x, y, null);
	}

	public static void load()
	{}

	/** @return The portrait for the input Pokemon. */
	public static BufferedImage portrait(Pokemon pokemon)
	{
		return portrait(pokemon.species(), pokemon.isShiny());
	}

	/** @return The portrait for the input Pokemon. */
	public static BufferedImage portrait(PokemonSpecies pokemon, boolean shiny)
	{
		PokemonPortrait sheet = normal;
		boolean alternative = pokemon.id >= 10000;
		if (shiny && pokemon.id >= 10000) sheet = alternateShinies;
		else if (pokemon.id >= 10000) sheet = alternates;
		else if (shiny) sheet = shinies;
		return sheet.getImg(pokemon.id - (alternative ? 10001 : 1));
	}

	private PokemonPortrait(String path, int lines)
	{
		super(path, PORTRAIT_SIZE, 400, lines * PORTRAIT_SIZE);
	}

}

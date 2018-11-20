package com.darkxell.client.resources.images.pokemon;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.darkxell.client.resources.images.RegularSpriteSet;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;

public class PokemonPortrait extends RegularSpriteSet
{
	public static enum PortraitEmotion
	{
		Amazed,
		Angry,
		Awkward,
		Confused,
		Crying,
		Determined,
		Disappointed,
		Happy,
		Ill,
		Joy,
		Laughing,
		Normal,
		Relieved,
		Shocked,
		Sneezing,
		Thoughtful;
	}

	private static final HashMap<PortraitEmotion, PokemonPortrait> alternateEmotions = new HashMap<>();
	private static final PokemonPortrait alternates = new PokemonPortrait("/pokemons/portraits/forms/normal.png", 20),
			alternateShinies = new PokemonPortrait("/pokemons/portraits/portraits/forms-shiny/normal.png", 20);
	private static final HashMap<PortraitEmotion, PokemonPortrait> alternateShinyEmotions = new HashMap<>();
	private static final PokemonPortrait normal = new PokemonPortrait("/pokemons/portraits/normal/normal.png", 100),
			shinies = new PokemonPortrait("/pokemons/portraits/shiny/normal.png", 100);
	private static final HashMap<PortraitEmotion, PokemonPortrait> normalEmotions = new HashMap<>();
	public static final int PORTRAIT_SIZE = 40;
	private static final HashMap<PortraitEmotion, PokemonPortrait> shinyEmotions = new HashMap<>();

	static
	{
		for (PortraitEmotion emotion : PortraitEmotion.values())
			if (emotion != PortraitEmotion.Normal)
			{
				alternateEmotions.put(emotion, new PokemonPortrait("/pokemons/portraits/forms/" + emotion.name().toLowerCase() + ".png", 20));
				alternateShinyEmotions.put(emotion,
						new PokemonPortrait("/pokemons/portraits/portraits/forms-shiny/" + emotion.name().toLowerCase() + ".png", 20));
				normalEmotions.put(emotion, new PokemonPortrait("/pokemons/portraits/normal/" + emotion + ".png", 100));
				shinyEmotions.put(emotion, new PokemonPortrait("/pokemons/portraits/shiny/" + emotion + ".png", 100));
			}
	}

	/** Draws the portrait of the input Pokemon at the input topright location. */
	public static void drawPortrait(Graphics2D g, Pokemon pokemon, int x, int y)
	{
		drawPortrait(g, pokemon, PortraitEmotion.Normal, x, y);
	}

	public static void drawPortrait(Graphics2D g, Pokemon pokemon, PortraitEmotion emotion, int x, int y)
	{
		drawPortrait(g, pokemon.species(), emotion, pokemon.isShiny(), x, y);
	}

	/** Draws the portrait of the input Pokemon at the input topright location. */
	public static void drawPortrait(Graphics2D g, PokemonSpecies pokemon, PortraitEmotion emotion, boolean shiny, int x, int y)
	{
		g.drawImage(PokemonPortrait.portrait(pokemon, emotion, shiny), x + 4, y + 4, null);
		g.drawImage(Sprites.Res_Hud.portrait.image(), x, y, null);
	}

	private static PokemonPortrait getEmotionSheet(PokemonPortrait sheet, PortraitEmotion emotion)
	{
		if (sheet == normal) return normalEmotions.get(emotion);
		if (sheet == shinies) return shinyEmotions.get(emotion);
		if (sheet == alternates) return alternateEmotions.get(emotion);
		if (sheet == alternateShinies) return alternateShinyEmotions.get(emotion);
		return sheet;
	}

	public static void load()
	{}

	/** @return The portrait for the input Pokemon. */
	public static BufferedImage portrait(Pokemon pokemon)
	{
		return portrait(pokemon, PortraitEmotion.Normal);
	}

	/** @return The portrait for the input Pokemon. */
	public static BufferedImage portrait(Pokemon pokemon, PortraitEmotion emotion)
	{
		return portrait(pokemon.species(), emotion, pokemon.isShiny());
	}

	/** @return The portrait for the input Pokemon. */
	public static BufferedImage portrait(PokemonSpecies pokemon, PortraitEmotion emotion, boolean shiny)
	{
		PokemonPortrait sheet = normal;
		boolean alternative = pokemon.id >= 10000;
		if (shiny && pokemon.id >= 10000) sheet = alternateShinies;
		else if (pokemon.id >= 10000) sheet = alternates;
		else if (shiny) sheet = shinies;

		if (emotion != null && emotion != PortraitEmotion.Normal) sheet = getEmotionSheet(sheet, emotion);

		return sheet.getImg(pokemon.id - (alternative ? 10001 : 1));
	}

	private PokemonPortrait(String path, int lines)
	{
		super(path, PORTRAIT_SIZE, 400, lines * PORTRAIT_SIZE);
	}

}

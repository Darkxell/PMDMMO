package com.darkxell.client.resources.images.pokemon;

import java.io.File;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.common.util.XMLUtils;

public final class PokemonSpritesets
{

	/** Stores the Spritesheets data. */
	private static final HashMap<Integer, Element> spritedata = new HashMap<Integer, Element>();
	/** Stores the loaded Spritesheets. */
	private static final HashMap<Integer, AbstractPokemonSpriteset> spritesets = new HashMap<Integer, AbstractPokemonSpriteset>();

	/** Disposes of the Spritesheet for the Pokémon with the input ID. */
	public static void disposeSpriteset(int id)
	{
		spritesets.remove(id);
	}

	/** Returns the Spritesheet for the Pokémon with the input ID. Loads it if not loaded. */
	public static AbstractPokemonSpriteset getSpriteset(int id)
	{
		if (!spritesets.containsKey(id)) loadSpriteset(id);
		return spritesets.get(id);
	}

	/** Reads the sprites data file. */
	public static void loadData()
	{
		Element xml = XMLUtils.readFile(new File("resources/data/spritesets.xml"));
		for (Element pokemon : xml.getChildren("pokemon"))
			spritedata.put(Integer.parseInt(pokemon.getAttributeValue("id")), pokemon);
		loadSpriteset(0);
	}

	/** Loads the Spritesheet for the Pokémon with the input ID. */
	private static void loadSpriteset(int id)
	{
		if (spritesets.containsKey(id)) return;
		Element xml = spritedata.get(id);
		if (xml == null)
		{
			spritesets.put(id, spritesets.get(0));
			return;
		}

		spritesets.put(id, new AbstractPokemonSpriteset("/pokemons/pkmn" + id + ".png", xml,id));
	}

	private PokemonSpritesets()
	{}

}

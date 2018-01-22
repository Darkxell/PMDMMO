package com.darkxell.client.resources.images.pokemon;

import java.io.File;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.client.resources.Res;
import com.darkxell.common.pokemon.Pokemon;
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

	public static AbstractPokemonSpriteset getSpriteset(Pokemon pokemon)
	{
		return getSpriteset(getSpritesetID(pokemon));
	}

	public static int getSpritesetID(Pokemon pokemon)
	{
		return (pokemon.isShiny ? -1 : 1) * pokemon.species.compoundID();
	}

	/** Reads the sprites data file. */
	public static void loadData()
	{
		Element xml = XMLUtils.readFile(new File("resources/pokemons/spritesets-data.xml"));
		for (Element pokemon : xml.getChildren("pokemon"))
			spritedata.put(Integer.parseInt(pokemon.getAttributeValue("id")), pokemon);
		loadSpriteset(0);
	}

	/** Loads the Spritesheet for the Pokémon with the input ID. */
	private static void loadSpriteset(int id)
	{
		if (spritesets.containsKey(id)) return;
		Element xml = spritedata.get(Math.abs(id));
		if (xml == null)
		{
			spritesets.put(id, spritesets.get(0));
			return;
		}

		if (id < 0 && Res.exists("/pokemons/pkmn" + (-id) + "s.png")) spritesets.put(id, new AbstractPokemonSpriteset("/pokemons/pkmn" + (-id) + "s.png", xml, -id));
		else spritesets.put(id, new AbstractPokemonSpriteset("/pokemons/pkmn" + Math.abs(id) + ".png", xml, Math.abs(id)));
	}

	private PokemonSpritesets()
	{}

}

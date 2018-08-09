package com.darkxell.client.resources.images.pokemon;

import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.client.resources.images.Res;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.XMLUtils;

public final class PokemonSpritesets
{

	/** Stores the loaded Spritesheets. */
	private static final HashMap<Integer, AbstractPokemonSpriteset> spritesets = new HashMap<Integer, AbstractPokemonSpriteset>();

	/** Disposes of the Spritesheet for the Pokemon with the input ID. */
	public static void disposeSpriteset(int id)
	{
		spritesets.remove(id);
	}

	/** Returns the Spritesheet for the Pokemon with the input ID. Loads it if not loaded. */
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
		return (pokemon.isShiny() ? -1 : 1) * pokemon.species().id;
	}

	/** Reads the sprites data file. */
	public static void loadData()
	{
		/* System.out.println("Loading Pokemon sprites..."); for (PokemonSpecies s : PokemonRegistry.list()) loadSpriteset(s.id); */
		loadSpriteset(0);
	}

	/** Loads the Spritesheet for the Pokemon with the input ID. */
	private static void loadSpriteset(int id)
	{
		String filename = Math.abs(id) + "";
		int effectiveID = Math.abs(id);
		if (id < 0 && Res.exists("/pokemons/" + effectiveID + "s.png")) filename = effectiveID + "s";
		else if (id < 0)
		{
			spritesets.put(id, spritesets.get(0));
			return;
		}

		if (spritesets.containsKey(id)) return;
		Element xml = XMLUtils.read(PokemonSpritesets.class.getResourceAsStream("/pokemons/data/" + effectiveID + ".xml"));
		if (xml == null)
		{
			spritesets.put(id, spritesets.get(0));
			return;
		}

		spritesets.put(id, new AbstractPokemonSpriteset("/pokemons/" + filename + ".png", xml, effectiveID));
	}

	private PokemonSpritesets()
	{}

}

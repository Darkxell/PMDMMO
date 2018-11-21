package com.darkxell.client.resources.images.pokemon;

import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.client.resources.Res;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.XMLUtils;

public final class PokemonSpritesets
{

	/** Stores the loaded Spriteset data. */
	private static final HashMap<Integer, PokemonSpritesetData> data = new HashMap<Integer, PokemonSpritesetData>();
	/** Stores the loaded Spritesheets. */
	private static final HashMap<Integer, PokemonSpriteset> spritesets = new HashMap<Integer, PokemonSpriteset>();

	/** Disposes of the Spritesheet for the Pokemon with the input ID. */
	public static void disposeSpriteset(int id)
	{
		spritesets.remove(id);
	}

	/** Returns the Spritesheet for the Pokemon with the input ID. Loads it if not loaded. */
	public static PokemonSpriteset getSpriteset(int id)
	{
		if (!spritesets.containsKey(id)) loadSpriteset(id);
		return spritesets.get(id);
	}

	public static PokemonSpriteset getSpriteset(Pokemon pokemon)
	{
		return getSpriteset(pokemon.species(), pokemon.isShiny());
	}

	public static PokemonSpriteset getSpriteset(PokemonSpecies species, boolean shiny)
	{
		return getSpriteset(getSpritesetID(species, shiny));
	}

	private static int getSpritesetID(PokemonSpecies species, boolean shiny)
	{
		return (shiny ? -1 : 1) * species.id;
	}

	/** Reads the sprites data file. */
	public static void loadData()
	{
		/* loadSpritesetData(0); Logger.d("Loading Pokemon sprite data..."); for (PokemonSpecies s : PokemonRegistry.list()) loadSpritesetData(s.id); */
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

		if (!data.containsKey(effectiveID)) loadSpritesetData(effectiveID);

		PokemonSpritesetData d = data.get(effectiveID);
		if (id != 0 && d == data.get(0))
		{
			spritesets.put(id, spritesets.get(0));
			return;
		}

		spritesets.put(id, new PokemonSpriteset("/pokemons/" + filename + ".png", data.get(effectiveID)));
	}

	public static void loadSpriteset(PokemonSpecies species, boolean shiny)
	{
		loadSpriteset(getSpritesetID(species, shiny));
	}

	/** Loads the animations data for the Pokemon with the input ID. */
	private static void loadSpritesetData(int id)
	{
		int effectiveID = Math.abs(id);
		if (data.containsKey(effectiveID)) return;
		Element xml = XMLUtils.read(PokemonSpritesets.class.getResourceAsStream("/pokemons/data/" + effectiveID + ".xml"));
		if (xml == null)
		{
			data.put(id, data.get(0));
			return;
		}

		data.put(id, new PokemonSpritesetData(effectiveID, xml));
	}

	private PokemonSpritesets()
	{}

}

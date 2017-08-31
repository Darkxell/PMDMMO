package com.darkxell.client.resources.images;

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
	}

	/** Loads the Spritesheet for the Pokémon with the input ID. */
	private static void loadSpriteset(int id)
	{
		if (spritesets.containsKey(id)) return;
		Element xml = spritedata.get(id);
		int width = 0, height = 0;
		if (xml.getAttribute("size") != null) width = height = Integer.parseInt(xml.getAttributeValue("size"));
		else
		{
			width = Integer.parseInt(xml.getAttributeValue("w"));
			height = Integer.parseInt(xml.getAttributeValue("h"));
		}
		int wake = xml.getAttribute("wake") == null ? 0 : Integer.parseInt(xml.getAttributeValue("wake"));
		int victory = xml.getAttribute("victory") == null ? 0 : Integer.parseInt(xml.getAttributeValue("victory"));

		String[] idle = xml.getAttributeValue("idle").split(",");
		int[] idleAnimation = new int[idle.length];
		for (int i = 0; i < idleAnimation.length; ++i)
			idleAnimation[i] = Integer.parseInt(idle[i]);

		spritesets.put(
				id,
				new AbstractPokemonSpriteset("/pokemons/pkmn" + id + ".png", Integer.parseInt(xml.getAttributeValue("x")), Integer.parseInt(xml
						.getAttributeValue("y")), width, height, idleAnimation, Integer.parseInt(xml.getAttributeValue("move")), Integer.parseInt(xml
						.getAttributeValue("attack")), Integer.parseInt(xml.getAttributeValue("spe1")), Integer.parseInt(xml.getAttributeValue("spe2")), "true"
						.equals(xml.getAttributeValue("ambient")), wake, victory));
	}

	private PokemonSpritesets()
	{}

}

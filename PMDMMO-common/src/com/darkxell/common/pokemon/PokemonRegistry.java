package com.darkxell.common.pokemon;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;

/** Holds all Pokémon species. */
public final class PokemonRegistry
{

	private static HashMap<Integer, PokemonSpecies> pokemon = new HashMap<Integer, PokemonSpecies>();

	/** @return The Pokémon species with the input ID. */
	public static PokemonSpecies find(int id)
	{
		return pokemon.get(id);
	}

	/** @return All Pokémon species. */
	public static Collection<PokemonSpecies> list()
	{
		return pokemon.values();
	}

	/** Loads this Registry for the Client. */
	public static void loadClient()
	{
		Logger.instance().debug("Loading Pokémon...");

		Element root = XMLUtils.readFile(new File("resources/data/pokemon.xml"));
		for (Element e : root.getChildren("pokemon"))
			try
			{
				PokemonSpecies species = new PokemonSpecies(e);
				pokemon.put(species.compoundID(), species);
				for (PokemonSpecies form : species.forms())
					pokemon.put(form.compoundID(), form);
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
	}

	/** Saves this Registry for the Client. */
	public static void saveClient()
	{
		Element species = new Element("species");
		for (PokemonSpecies pk : pokemon.values())
			if (pk.formID == 0) species.addContent(pk.toXML());
		XMLUtils.saveFile(new File("resources/data/pokemon.xml"), species);
	}

}

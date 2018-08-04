package com.darkxell.common.pokemon;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;

/** Holds all Pokemon species. */
public final class PokemonRegistry
{

	private static HashMap<Integer, PokemonSpecies> pokemon = new HashMap<Integer, PokemonSpecies>();

	/** @return The Pokemon species with the input ID. */
	public static PokemonSpecies find(int id)
	{
		if (!pokemon.containsKey(id))
		{
			Logger.e("There is no Pokemon with ID " + id + ".");
			return null;
		} // else if (id == 0) Logger.w("Using default Pokemon!");
		return pokemon.get(id);
	}

	/** @return All Pokemon species. */
	public static Collection<PokemonSpecies> list()
	{
		return pokemon.values();
	}

	/** Loads this Registry for the Client. */
	public static void load()
	{
		Logger.instance().debug("Loading Pokemon...");
		Element root = XMLUtils.read(PokemonRegistry.class.getResourceAsStream("/data/pokemon.xml"));
                
		for (Element e : root.getChildren("pokemon", root.getNamespace()))
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

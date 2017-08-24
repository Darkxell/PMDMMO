package com.darkxell.common.pokemon;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/** Holds all Pok�mon species. */
public final class PokemonRegistry
{

	private static HashMap<Integer, PokemonSpecies> pokemon = new HashMap<Integer, PokemonSpecies>();

	/** @return The Pok�mon species with the input ID. */
	public static PokemonSpecies find(int id)
	{
		return pokemon.get(id);
	}

	/** @return All Pok�mon species. */
	public static Collection<PokemonSpecies> list()
	{
		return pokemon.values();
	}

	/** Loads this Registry for the Client. */
	public static void loadClient()
	{
		System.out.println("Loading Pok�mon...");
		
		File file = new File("resources/data/pokemon.xml");
		SAXBuilder builder = new SAXBuilder();
		try
		{
			Element root = builder.build(file).getRootElement();
			for (Element e : root.getChildren("pokemon"))
			{
				PokemonSpecies species = new PokemonSpecies(e);
				pokemon.put(species.id, species);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}

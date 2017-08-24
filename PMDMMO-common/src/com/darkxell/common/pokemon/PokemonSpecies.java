package com.darkxell.common.pokemon;

import java.util.ArrayList;
import java.util.HashMap;

public class PokemonSpecies
{

	/** Base experience. */
	public final int baseXP;
	/** List of species this Pokémon can evolve into. */
	private final ArrayList<Evolution> evolutions;
	public final int height, weight;
	public final int id, formID;
	/** List of moves learned by leveling up. Key is level, value is the list of move IDs. */
	private final HashMap<Integer, ArrayList<Integer>> learnset;
	public final String name, formName;
	/** Recruitment rate. */
	public final int recruitRate;
	public final PokemonStats stats;
	/** List of TMs that can be taught. */
	private final ArrayList<Integer> tms;
	/** This Pokémon's types. type2 can be null. */
	public final PokemonType type1, type2;

	public PokemonSpecies(int id, String name, int formID, String formName, PokemonType type1, PokemonType type2, int baseXP, PokemonStats stats, int height,
			int weight, int recruitRate)
	{
		this.id = id;
		this.name = name;
		this.formID = formID;
		this.formName = formName;
		this.type1 = type1;
		this.type2 = type2;
		this.baseXP = baseXP;
		this.stats = stats;
		this.height = height;
		this.weight = weight;
		this.recruitRate = recruitRate;
		this.learnset = new HashMap<Integer, ArrayList<Integer>>();
		this.tms = new ArrayList<Integer>();
		this.evolutions = new ArrayList<Evolution>();
	}

}

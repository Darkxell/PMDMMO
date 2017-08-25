package com.darkxell.common.dungeon;

import java.util.ArrayList;

import org.jdom2.Element;

/** Describes a Dungeon: floors, Pokémon, items... */
public class Dungeon
{

	/** Lists the Items found in this Dungeon. */
	private ArrayList<DungeonItem> buriedItems;
	/** Lists this Dungeon's floors that are not random. */
	private ArrayList<Integer> cutsceneFloors;
	/** The number of Floors in this Dungeon. */
	public final int floorCount;
	/** This Dungeon's ID. */
	public final int id;
	/** Lists the Items found in this Dungeon. */
	private ArrayList<DungeonItem> items;
	/** Lists the Items found in this Dungeon. */
	private ArrayList<DungeonItem> monsterHouseItems;
	/** Lists the Pokémon found in this Dungeon. */
	private ArrayList<DungeonPokemon> pokemon;
	/** Lists the Traps found in this Dungeon. */
	private ArrayList<DungeonTrap> traps;

	public Dungeon(Element xml)
	{
		// TODO Dungeon(xml)
	}

	public Dungeon(int id, int floorCount, ArrayList<Integer> cutsceneFloors, ArrayList<DungeonPokemon> pokemon, ArrayList<DungeonItem> items,
			ArrayList<DungeonItem> monsterHouseItems, ArrayList<DungeonItem> buriedItems, ArrayList<DungeonTrap> traps)
	{
		this.id = id;
		this.floorCount = floorCount;
		this.cutsceneFloors = cutsceneFloors;
		this.pokemon = pokemon;
		this.items = items;
		this.monsterHouseItems = monsterHouseItems;
		this.buriedItems = buriedItems;
		this.traps = traps;
	}

}

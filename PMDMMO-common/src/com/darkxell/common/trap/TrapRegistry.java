package com.darkxell.common.trap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/** Holds all Traps. */
public final class TrapRegistry
{

	static HashMap<Integer, Trap> traps = new HashMap<Integer, Trap>();
	public static final Trap WONDER_TILE = new Trap(0) {};

	/** @return The Trap with the input ID. */
	public static Trap find(int id)
	{
		return traps.get(id);
	}

	/** @return All Traps. */
	public static ArrayList<Trap> list()
	{
		ArrayList<Trap> list = new ArrayList<>(traps.values());
		list.sort(Comparator.naturalOrder());
		return list;
	}

	public static void load()
	{
		for (int i = 1; i < 18; i++)
			new Trap(i) {};
	}

}

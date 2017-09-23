package com.darkxell.common.trap;

import java.util.Collection;
import java.util.HashMap;

/** Holds all Traps. */
public final class TrapRegistry
{

	static HashMap<Integer, Trap> traps = new HashMap<Integer, Trap>();

	/** @return The Trap with the input ID. */
	public static Trap find(int id)
	{
		return traps.get(id);
	}

	/** @return All Traps. */
	public static Collection<Trap> list()
	{
		return traps.values();
	}

	public static void load()
	{
		for (int i = 0; i < 17; i++)
			new Trap(i)
			{};
	}

}

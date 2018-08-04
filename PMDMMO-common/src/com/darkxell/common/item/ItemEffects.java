package com.darkxell.common.item;

import java.util.Collection;
import java.util.HashMap;

/** Holds all Move Effects. */
public final class ItemEffects
{
	static final HashMap<Integer, ItemEffect> effects = new HashMap<Integer, ItemEffect>();

	public static final ItemEffect Default = new ItemEffect(-1);
	public static final ItemEffect Pokedollars = new ItemEffect(0);
	public static final ItemEffect XRaySpecs = new ItemEffect(208);

	/** @return The Effect with the input ID. */
	public static ItemEffect find(int id)
	{
		if (!effects.containsKey(id)) return Default;
		return effects.get(id);
	}

	/** @return All Effects. */
	public static Collection<ItemEffect> list()
	{
		return effects.values();
	}

}

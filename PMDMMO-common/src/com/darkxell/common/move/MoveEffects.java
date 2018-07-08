package com.darkxell.common.move;

import java.util.Collection;
import java.util.HashMap;

import com.darkxell.common.move.effects.StatChangeEffect;
import com.darkxell.common.pokemon.BaseStats.Stat;

/** Holds all Move Effects. */
public final class MoveEffects
{
	static final HashMap<Integer, MoveEffect> effects = new HashMap<Integer, MoveEffect>();

	public static final MoveEffect Default = new MoveEffect(0);
	public static final MoveEffect Basic_attack = new MoveEffect(1);
	public static final MoveEffect Lower_attack = new StatChangeEffect(86, Stat.Attack, -1);

	/** @return The Effect with the input ID. */
	public static MoveEffect find(int id)
	{
		if (!effects.containsKey(id)) return Default;
		return effects.get(id);
	}

	/** @return All Effects. */
	public static Collection<MoveEffect> list()
	{
		return effects.values();
	}

}

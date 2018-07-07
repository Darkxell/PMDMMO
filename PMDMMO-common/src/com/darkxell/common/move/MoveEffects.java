package com.darkxell.common.move;

import java.util.Collection;
import java.util.HashMap;

import com.darkxell.common.move.effects.DealDamageEffect;

/** Holds all Move Effects. */
public final class MoveEffects
{
	static final HashMap<Integer, MoveEffect> effects = new HashMap<Integer, MoveEffect>();

	public static final MoveEffect Default = new DealDamageEffect(0);
	public static final MoveEffect Basic_attack = new DealDamageEffect(1);

	/** @return The Effect with the input ID. */
	public static MoveEffect find(int id)
	{
		return effects.get(id);
	}

	/** @return All Effects. */
	public static Collection<MoveEffect> list()
	{
		return effects.values();
	}

}

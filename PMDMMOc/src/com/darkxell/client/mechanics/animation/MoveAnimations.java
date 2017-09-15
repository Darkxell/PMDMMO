package com.darkxell.client.mechanics.animation;

import com.darkxell.client.mechanics.animation.moves.BasicAttackAnimation;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.DungeonPokemon;

public class MoveAnimations
{

	public static AbstractAnimation createAnimation(AnimationEndListener listener, DungeonPokemon user, Move move)
	{

		// By default, return the basic attack animation

		return new BasicAttackAnimation(user, listener);
	}

	/** May return null if the Move doesn't have a Target animation. */
	public static AbstractAnimation createTargetAnimation(AnimationEndListener listener, DungeonPokemon user, Move move)
	{
		return null;
	}
}

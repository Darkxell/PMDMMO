package com.darkxell.client.state.dungeon;

import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.common.pokemon.DungeonPokemon;

public class OrbAnimationState extends AnimationState implements AnimationEndListener
{

	/** The State to set when this Animation ends. */
	public final AnimationState next;

	public OrbAnimationState(DungeonState parent, DungeonPokemon user, AnimationState next)
	{
		super(parent);
		this.next = next;
		this.animation = Animations.getOrbAnimation(user, this);
	}

	@Override
	public void onAnimationEnd(AbstractAnimation animation)
	{
		this.parent.setSubstate(this.next);
	}

}

package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.DungeonEventProcessor;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.MoveAnimations;
import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;

public class MoveAnimationState extends DungeonSubState implements AnimationEndListener
{

	public final AbstractAnimation animation;
	public final LearnedMove move;
	public final DungeonPokemon user;

	public MoveAnimationState(DungeonState parent, DungeonPokemon user, LearnedMove move)
	{
		super(parent);
		this.move = move;
		this.user = user;
		this.animation = MoveAnimations.createAnimation(this, user, move.move());
	}

	@Override
	public void onAnimationEnd(AbstractAnimation animation)
	{
		this.parent.setSubstate(this.parent.actionSelectionState);
		DungeonEventProcessor.addToPending(this.move.move().prepareUse(this.user, this.move, DungeonPersistance.floor));
		DungeonEventProcessor.processPending();
	}

	@Override
	public void onKeyPressed(short key)
	{}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void onStart()
	{
		super.onStart();
		this.animation.start();
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		this.animation.render(g, width, height);
	}

	@Override
	public void update()
	{}

}

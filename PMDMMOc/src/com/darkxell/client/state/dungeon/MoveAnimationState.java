package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.MoveAnimations;
import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.common.event.MoveTarget;
import com.darkxell.common.event.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public class MoveAnimationState extends DungeonSubState implements AnimationEndListener
{

	public final AbstractAnimation animation;
	public final Move move;
	public final DungeonPokemon user;

	public MoveAnimationState(DungeonState parent, DungeonPokemon user, Move move)
	{
		super(parent);
		this.move = move;
		this.user = user;
		this.animation = MoveAnimations.createAnimation(this, user, move);
	}

	@Override
	public void onAnimationEnd(AbstractAnimation animation)
	{
		MoveUseEvent e = this.move.use(this.user, DungeonPersistance.floor);
		this.parent.setSubstate(this.parent.actionSelectionState);
		for (MoveTarget target : e.targets())
			this.parent.logger.showMessage(target.resultMessage());
		if (e.targets().length == 0 && this.move != MoveRegistry.ATTACK) this.parent.logger.showMessage(new Message("move.no_target"));
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
		if (this.move != MoveRegistry.ATTACK) this.parent.logger.showMessage(new Message("move.used").addReplacement("<pokemon>",
				this.user.pokemon.getNickname()).addReplacement("<move>", this.move.name()));
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

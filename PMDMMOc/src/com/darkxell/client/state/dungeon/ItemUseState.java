package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.renderers.ItemRenderer;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.common.item.Item;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

public class ItemUseState extends DungeonSubState implements AnimationEndListener
{

	public final AbstractAnimation animation;
	public final Item item;
	public final DungeonPokemon user;

	public ItemUseState(DungeonState parent, DungeonPokemon user, Item item)
	{
		super(parent);
		this.user = user;
		this.item = item;
		this.animation = ItemRenderer.createItemAnimation(this.item, this.user, this.parent.floor, this);
	}

	@Override
	public void onAnimationEnd(AbstractAnimation animation)
	{
		for (Message m : this.item.use(this.user))
			this.parent.logger.showMessage(m);
		this.parent.setSubstate(this.parent.actionSelectionState);
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

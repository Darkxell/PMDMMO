package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.renderers.ItemRenderer;
import com.darkxell.client.state.FreezoneExploreState;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.common.event.ItemUseEvent;
import com.darkxell.common.item.ItemEscapeOrb;
import com.darkxell.common.util.Message;

public class ItemUseState extends DungeonSubState implements AnimationEndListener {

	public final AbstractAnimation animation;
	public final ItemUseEvent event;

	public ItemUseState(DungeonState parent, ItemUseEvent event) {
		super(parent);
		this.event = event;
		this.animation = ItemRenderer.createItemAnimation(this.event, this);
	}

	@Override
	public void onAnimationEnd(AbstractAnimation animation) {
		for (Message m : this.event.getMessages())
			this.parent.logger.showMessage(m);
		this.parent.setSubstate(this.parent.actionSelectionState);

		this.processItemEffect();
	}

	@Override
	public void onKeyPressed(short key) {
	}

	@Override
	public void onKeyReleased(short key) {
	}

	@Override
	public void onStart() {
		super.onStart();
		this.animation.start();
	}

	private void processItemEffect() {
		if (this.event.item instanceof ItemEscapeOrb)
			Launcher.stateManager.setState(new FreezoneExploreState());
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		this.animation.render(g, width, height);
	}

	@Override
	public void update() {
	}

}

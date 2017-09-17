package com.darkxell.client.state;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.renderers.layers.BackgroundSeaLayer;
import com.darkxell.client.ui.Keys;

public class OpenningState extends AbstractState {

	private BackgroundSeaLayer background = new BackgroundSeaLayer();

	@Override
	public void onKeyPressed(short key) {
		if (key == Keys.KEY_ATTACK)
			Launcher.stateManager.setState(new FreezoneExploreState());
	}

	@Override
	public void onKeyReleased(short key) {
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		background.render(g, width, height);

	}

	@Override
	public void update() {
		background.update();

	}

}

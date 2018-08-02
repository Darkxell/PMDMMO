package com.darkxell.client.state.menu.freezone;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys.Key;

public class MissionBoardState extends AbstractState {

	private AbstractState exploresource;

	public MissionBoardState(AbstractState exploresource) {
		this.exploresource = exploresource;
	}

	@Override
	public void onKeyPressed(Key key) {
		switch (key) {
		case RUN:
			Persistance.stateManager.setState(new MissionBoardSelectionState(this.exploresource));
			break;
		default:
			break;
		}
	}

	@Override
	public void onKeyReleased(Key key) {

	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		this.exploresource.render(g, width, height);
		g.setColor(new Color(100, 100, 100, 100));
		g.fillRect(0, 0, width, height);

		String s = "WIP, this was my branch. 4th wall demolition hello.";
		TextRenderer.render(g, s, width / 2 - (TextRenderer.width(s) / 2), height / 2);
		s = "Press return to go back to explorestate.";
		TextRenderer.render(g, s, width / 2 - (TextRenderer.width(s) / 2), height / 2 + 20);
	}

	@Override
	public void update() {
		this.exploresource.update();
	}

}

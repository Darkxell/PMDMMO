package com.darkxell.client.state.mainstates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.menu.menus.LanguageSelectionMenuState;
import com.darkxell.client.state.menu.menus.LanguageSelectionMenuState.LanguageSelectionListener;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.language.Localization.Language;

public class ChooseLanguageMainState extends StateManager implements LanguageSelectionListener {

	private LanguageSelectionMenuState menu;

	public ChooseLanguageMainState() {
		this.menu = new LanguageSelectionMenuState(null, null, true);
		this.menu.listener = this;
	}

	@Override
	public void onKeyPressed(KeyEvent e, Key key) {
		this.menu.onKeyPressed(key);
	}

	@Override
	public void onKeyReleased(KeyEvent e, Key key) {
		this.menu.onKeyReleased(key);
	}

	@Override
	public void onKeyTyped(KeyEvent e) {
		this.menu.onKeyTyped(e);
	}

	@Override
	public void onLangSelected(Language lang) {
		Persistence.stateManager = new LoginMainState();
	}

	@Override
	public void onMouseClick(int x, int y) {
		this.menu.onMouseClick(x, y);
	}

	@Override
	public void onMouseMove(int x, int y) {
		this.menu.onMouseMove(x, y);
	}

	@Override
	public void onMouseRightClick(int x, int y) {
		this.menu.onMouseRightClick(x, y);
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		this.menu.render(g, width, height);
	}

	@Override
	public AbstractState getCurrentState() {
		return null;
	}

	@Override
	public void update() {
		this.menu.update();
	}

}

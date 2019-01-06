package com.darkxell.client.state.mainstates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.ClientSettings.Setting;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.language.Localization.Language;

public class LoadingMainState extends StateManager {

	private String loadingMessage = "Loading";

	@Override
	public void onKeyPressed(KeyEvent e, Key key) {}

	@Override
	public void onKeyReleased(KeyEvent e, Key key) {}

	@Override
	public void onKeyTyped(KeyEvent e) {}

	public void onLoadingFinished() {
		if (ClientSettings.getSetting(Setting.LANGUAGE).equals("null")) {
			if (Language.values().length == 1) {
				ClientSettings.setSetting(Setting.LANGUAGE, Language.values()[0].id);
				Persistence.stateManager = new LoginMainState();
			} else Persistence.stateManager = new ChooseLanguageMainState();
		} else Persistence.stateManager = new LoginMainState();
	}

	@Override
	public void onMouseClick(int x, int y) {}

	@Override
	public void onMouseMove(int x, int y) {}

	@Override
	public void onMouseRightClick(int x, int y) {}

	@Override
	public void render(Graphics2D g, int width, int height) {

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		String pmdo = "Pokemon Mystery Dungeon Online";
		TextRenderer.render(g, pmdo, width / 2 - TextRenderer.width(pmdo) / 2,
				height / 2 - TextRenderer.height() * 3 / 2);

		TextRenderer.render(g, this.loadingMessage, width / 2 - TextRenderer.width(this.loadingMessage) / 2,
				height / 2 + TextRenderer.height() / 2);
	}

	@Override
	public void update() {}

}

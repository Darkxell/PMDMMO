package com.darkxell.client.state.mainstates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.client.ui.MainUiUtility;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.Position;
import com.darkxell.common.util.language.Message;

public class AccountCreatedMainState extends StateManager {

	public AccountCreatedMainState(String p, String l) {
		super();
		pass = p;
		login = l;
		this.welcomeM = new Message("ui.login.welcome").addReplacement("<name>", this.login);
		this.textM = new Message("ui.login.accountcreated");
		this.proceedM = new Message("ui.login.proceed");
	}

	private String login = "";
	private String pass = "";

	/** The width of the non responsive square containing the login componnents. */
	private static final int squarewidth = 500;
	/** The height of the non responsive square containing the login componnents. */
	private static final int squareheight = 350;
	private int mouseX = 1;
	private int mouseY = 1;
	private int offsetx = 100;
	private int offsety = 20;

	private final Message welcomeM, textM, proceedM;

	@Override
	public void onKeyPressed(KeyEvent e, Key key) {}

	@Override
	public void onKeyReleased(KeyEvent e, Key key) {}

	@Override
	public void onKeyTyped(KeyEvent e) {}

	@Override
	public void onMouseClick(int x, int y) {
		// Button clicks
		if (button_proceed.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			Persistence.stateManager = new LoginMainState(new Message("ui.login.after_creation"), login, pass);
		}
	}

	@Override
	public void onMouseMove(int x, int y) {
		this.mouseX = x;
		this.mouseY = y;
	}

	@Override
	public void onMouseRightClick(int x, int y) {

	}

	private DoubleRectangle button_proceed = new DoubleRectangle(211, 301, 128, 38);

	@Override
	public void render(Graphics2D g, int width, int height) {
		MainUiUtility.drawBackground(g, width, height, (byte) 2);
		offsetx = width / 2 - squarewidth / 2;
		offsety = height / 2 - squareheight / 2;
		int relativemousex = mouseX - offsetx, relativemousey = mouseY - offsety;
		g.translate(offsetx, offsety);
		// DRAWS THE ACCOUNT CREATION FACILITIES
		g.drawImage(Sprites.Res_Hud.proceedaccountframe.image(), 0, 0, null);

		g.setColor(Color.BLACK);
		TextRenderer.render(g, this.welcomeM.toString(), 30, 150);
		ArrayList<String> lines = TextRenderer.splitLines(this.textM.toString(), 480);
		for (int i = 0; i < lines.size(); ++i)
			TextRenderer.render(g, lines.get(i), 30, 170 + (20 * i));

		Color buttoncolor = new Color(124, 163, 255), selectedbuttoncolor = new Color(183, 222, 255);
		g.setColor((button_proceed.isInside(new Position(relativemousex, relativemousey))) ? selectedbuttoncolor
				: buttoncolor);
		g.fillRect((int) button_proceed.x, (int) button_proceed.y, (int) button_proceed.width,
				(int) button_proceed.height);
		g.setColor(Color.BLACK);
		g.drawString(this.proceedM.toString(), (int) button_proceed.x + 8, (int) button_proceed.y + 25);

		// REVERT GRAPHICS
		g.translate(-offsetx, -offsety);
	}

	@Override
	public void update() {}

}

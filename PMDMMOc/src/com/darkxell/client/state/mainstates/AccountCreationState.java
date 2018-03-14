package com.darkxell.client.state.mainstates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.chat.CustomTextfield;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.ui.MainUiUtility;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.Position;
import com.eclipsesource.json.JsonObject;

public class AccountCreationState extends StateManager {

	private CustomTextfield login = new CustomTextfield();
	private CustomTextfield password = new CustomTextfield();
	/**
	 * The width of the non responsive square containing the login componnents.
	 * I'm lazy.
	 */
	private static final int squarewidth = 500;
	/**
	 * The height of the non responsive square containing the login componnents.
	 * I'm still lazy.
	 */
	private static final int squareheight = 350;
	private int mouseX = 1;
	private int mouseY = 1;
	private int offsetx = 100;
	private int offsety = 20;
	private boolean focus_login = true;
	private boolean focus_pass = false;

	@Override
	public void onKeyPressed(KeyEvent e, short key) {

	}

	@Override
	public void onKeyReleased(KeyEvent e, short key) {

	}

	@Override
	public void onKeyTyped(KeyEvent e) {
		if (focus_login)
			this.login.insertString(e.getKeyChar() + "");
		if (focus_pass)
			this.password.insertString(e.getKeyChar() + "");
	}

	@Override
	public void onMouseClick(int x, int y) {
		if (button_create.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			Persistance.stateManager = new LoginMainState();
			// Sends the account creation payload to the server
			String message = "";
			JsonObject mess = new JsonObject().add("action", "createaccount").add("name", this.login.getContent())
					.add("passhash", this.password.getContent());
			message = mess.toString();
			Persistance.socketendpoint.sendMessage(message);
			System.out.println(message);
		}
		if (textfield_login.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			this.login.setSelection(focus_login = true);
			this.password.setSelection(focus_pass = false);
		} else if (textfield_pass.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			this.login.setSelection(focus_login = false);
			this.password.setSelection(focus_pass = true);
		} else {
			this.login.setSelection(focus_login = false);
			this.password.setSelection(focus_pass = false);
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

	private DoubleRectangle textfield_login = new DoubleRectangle(40, 30, 400, 45);
	private DoubleRectangle textfield_pass = new DoubleRectangle(40, 130, 400, 45);
	private DoubleRectangle button_create = new DoubleRectangle(150, 230, 150, 55);

	@Override
	public void render(Graphics2D g, int width, int height) {
		MainUiUtility.drawBackground(g, width, height, (byte) 2);
		offsetx = width / 2 - squarewidth / 2;
		offsety = height / 2 - squareheight / 2;
		int relativemousex = mouseX - offsetx, relativemousey = mouseY - offsety;
		g.translate(offsetx, offsety);
		// DRAWS THE LOGIN FACILITIES
		g.setColor(new Color(63, 197, 255, 105));
		g.fillRect(0, 0, squarewidth, squareheight);
		MainUiUtility.drawBoxOutline(g, 0, 0, squarewidth, squareheight);
		g.setColor(Color.BLUE);
		g.fillRect((int) textfield_login.x, (int) textfield_login.y, (int) textfield_login.width,
				(int) textfield_login.height);
		g.fillRect((int) textfield_pass.x, (int) textfield_pass.y, (int) textfield_pass.width,
				(int) textfield_pass.height);

		g.translate(textfield_login.x + 10, textfield_login.y);
		this.login.render(g, (int) textfield_login.width - 20, (int) textfield_login.height - 15);
		g.translate(-textfield_login.x - 10, -textfield_login.y);

		g.translate(textfield_pass.x + 10, textfield_pass.y);
		this.password.render(g, (int) textfield_pass.width - 20, (int) textfield_pass.height - 15);
		g.translate(-textfield_pass.x - 10, -textfield_pass.y);

		g.setColor(Color.WHITE);
		g.drawString("LOGIN", (int) textfield_login.x, (int) textfield_login.y - 10);
		g.drawString("PASSWORD", (int) textfield_pass.x, (int) textfield_pass.y - 10);

		g.setColor((button_create.isInside(new Position(relativemousex, relativemousey))) ? Color.CYAN : Color.GREEN);
		g.fillRect((int) button_create.x, (int) button_create.y, (int) button_create.width, (int) button_create.height);
		g.setColor(Color.BLACK);
		g.drawString("CREATE ACCOUNT", (int) button_create.x + 20, (int) button_create.y + 30);

		// REVERT GRAPHICS
		g.translate(-offsetx, -offsety);
	}

	@Override
	public void update() {
		this.login.update();
		this.password.update();
		if (firstupdate) {
			firstupdate = false;
			this.password.setSelection(false);
		}
	}

	private boolean firstupdate = true;
}
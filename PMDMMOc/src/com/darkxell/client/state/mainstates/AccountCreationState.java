package com.darkxell.client.state.mainstates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.chat.CustomTextfield;
import com.darkxell.client.resources.images.others.Hud;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.ui.MainUiUtility;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.Position;
import com.eclipsesource.json.JsonObject;

public class AccountCreationState extends StateManager {

	private CustomTextfield login = new CustomTextfield();
	private CustomTextfield password = new CustomTextfield();
	private CustomTextfield confirm = new CustomTextfield();
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
	
	private String errormessage = "Template error message";
	
	@Override
	public void onKeyPressed(KeyEvent e, short key) {
		login.onKeyPressed(e);
		password.onKeyPressed(e);
		confirm.onKeyPressed(e);
	}

	@Override
	public void onKeyReleased(KeyEvent e, short key) {

	}

	@Override
	public void onKeyTyped(KeyEvent e) {
		login.onKeyTyped(e);
		password.onKeyTyped(e);
		confirm.onKeyTyped(e);
	}

	@Override
	public void onMouseClick(int x, int y) {
		// Button clicks
		if (button_create.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			Persistance.stateManager = new LoginMainState();
			// Sends the account creation payload to the server
			String message = "";
			JsonObject mess = new JsonObject().add("action", "createaccount").add("name", this.login.getContent())
					.add("passhash", this.password.getContent());
			message = mess.toString();
			Persistance.socketendpoint.sendMessage(message);
		} else if (button_back.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			Persistance.stateManager = new LoginMainState();
		}
		// Textfield focus
		if (textfield_login.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			this.login.setSelection(true);
			this.password.setSelection(false);
			this.confirm.setSelection(false);
		} else if (textfield_pass.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			this.login.setSelection(false);
			this.password.setSelection(true);
			this.confirm.setSelection(false);
		} else if (textfield_confirm.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			this.login.setSelection(false);
			this.password.setSelection(false);
			this.confirm.setSelection(true);
		} else {
			this.login.setSelection(false);
			this.password.setSelection(false);
			this.confirm.setSelection(false);
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

	private DoubleRectangle textfield_login = new DoubleRectangle(155, 157, 257, 32);
	private DoubleRectangle textfield_pass = new DoubleRectangle(155, 203, 257, 32);
	private DoubleRectangle textfield_confirm = new DoubleRectangle(155, 249, 257, 32);
	private DoubleRectangle button_create = new DoubleRectangle(211, 301, 128, 38);
	private DoubleRectangle button_back = new DoubleRectangle(33, 150, 82, 27);

	@Override
	public void render(Graphics2D g, int width, int height) {
		MainUiUtility.drawBackground(g, width, height, (byte) 2);
		offsetx = width / 2 - squarewidth / 2;
		offsety = height / 2 - squareheight / 2;
		int relativemousex = mouseX - offsetx, relativemousey = mouseY - offsety;
		g.translate(offsetx, offsety);
		// DRAWS THE ACCOUNT CREATION FACILITIES
		g.drawImage(Hud.createaccountframe, 0, 0, null);
		
		g.translate(textfield_login.x + 10, textfield_login.y);
		this.login.render(g, (int) textfield_login.width - 20, (int) textfield_login.height - 15);
		g.translate(-textfield_login.x - 10, -textfield_login.y);

		g.translate(textfield_pass.x + 10, textfield_pass.y);
		this.password.render(g, (int) textfield_pass.width - 20, (int) textfield_pass.height - 15);
		g.translate(-textfield_pass.x - 10, -textfield_pass.y);

		g.translate(textfield_confirm.x + 10, textfield_confirm.y);
		this.confirm.render(g, (int) textfield_confirm.width - 20, (int) textfield_confirm.height - 15);
		g.translate(-textfield_confirm.x - 10, -textfield_confirm.y);

		Color buttoncolor = new Color(124, 163, 255), selectedbuttoncolor = new Color(183, 222, 255);
		g.setColor((button_create.isInside(new Position(relativemousex, relativemousey))) ? selectedbuttoncolor
				: buttoncolor);
		g.fillRect((int) button_create.x, (int) button_create.y, (int) button_create.width, (int) button_create.height);
		g.setColor(Color.BLACK);
		g.drawString("CREATE ACCOUNT", (int) button_create.x + 8, (int) button_create.y + 25);

		g.setColor((button_back.isInside(new Position(relativemousex, relativemousey))) ? selectedbuttoncolor
				: buttoncolor);
		g.fillRect((int) button_back.x, (int) button_back.y, (int) button_back.width, (int) button_back.height);
		g.setColor(Color.BLACK);
		g.drawString("BACK", (int) button_back.x + 10, (int) button_back.y + 15);

		// REVERT GRAPHICS
		g.translate(-offsetx, -offsety);
	}

	@Override
	public void update() {
		this.login.update();
		this.password.update();
		this.confirm.update();
		if (firstupdate) {
			firstupdate = false;
			this.password.setSelection(false);
			this.confirm.setSelection(false);
		}
	}

	private boolean firstupdate = true;
}
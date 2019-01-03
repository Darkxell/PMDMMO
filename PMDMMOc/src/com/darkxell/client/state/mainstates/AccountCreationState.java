package com.darkxell.client.state.mainstates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.launchable.crypto.Encryption;
import com.darkxell.client.mechanics.chat.CustomTextfield;
import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.client.ui.MainUiUtility;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.Position;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.JsonObject;

public class AccountCreationState extends StateManager {

	private CustomTextfield login = new CustomTextfield();
	private CustomTextfield password = new CustomTextfield().setObfuscated();
	private CustomTextfield confirm = new CustomTextfield().setObfuscated();
	private CustomTextfield deploykey = new CustomTextfield();
	/** The width of the non responsive square containing the login componnents. I'm lazy. */
	private static final int squarewidth = 500;
	/** The height of the non responsive square containing the login componnents. I'm still lazy. */
	private static final int squareheight = 350;
	private int mouseX = 1;
	private int mouseY = 1;
	private int offsetx = 100;
	private int offsety = 20;

	private String errormessage = "";
	private String assessmessage = "";

	private final Message titleM, usernameM, passwordM, confirmM, deployM, createM, backM;

	public AccountCreationState() {
		this.titleM = new Message("ui.create_account");
		this.usernameM = new Message("ui.username");
		this.passwordM = new Message("ui.password");
		this.confirmM = new Message("ui.create_account.confirm");
		this.deployM = new Message("ui.create_account.deploy");
		this.createM = new Message("ui.login.create_account");
		this.backM = new Message("general.back");
	}

	@Override
	public void onKeyPressed(KeyEvent e, Key key) {
		login.onKeyPressed(e);
		password.onKeyPressed(e);
		confirm.onKeyPressed(e);
		deploykey.onKeyPressed(e);
	}

	@Override
	public void onKeyReleased(KeyEvent e, Key key) {

	}

	@Override
	public void onKeyTyped(KeyEvent e) {
		login.onKeyTyped(e);
		password.onKeyTyped(e);
		confirm.onKeyTyped(e);
		deploykey.onKeyTyped(e);
	}

	@Override
	public void onMouseClick(int x, int y) {
		// Button clicks
		if (button_create.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			if (login.getContent().length() < 2) {
				this.errormessage = new Message("ui.login.namelengtherror").toString();
			} else if (password.getContent().length() < 8) {
				this.errormessage = new Message("ui.login.passtooshort").toString();
			} else if (!password.getContent().equals(confirm.getContent())) {
				this.errormessage = new Message("ui.login.passconfirmerror").toString();
			} else if (Persistence.socketendpoint == null
					|| Persistence.socketendpoint.connectionStatus() != GameSocketEndpoint.CONNECTED) {
				this.errormessage = new Message("ui.login.noconnection").toString();
			} else {
				// Sends the account creation payload to the server
				sendAccountcreationMessage();
				this.assessmessage = new Message("ui.login.serverwait").toString();
				this.errormessage = "";
			}
		} else if (button_back.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			Persistence.stateManager = new LoginMainState();
		}
		// Textfield focus
		if (textfield_login.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			this.login.setSelection(true);
			this.password.setSelection(false);
			this.confirm.setSelection(false);
			this.deploykey.setSelection(false);
		} else if (textfield_pass.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			this.login.setSelection(false);
			this.password.setSelection(true);
			this.confirm.setSelection(false);
			this.deploykey.setSelection(false);
		} else if (textfield_confirm.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			this.login.setSelection(false);
			this.password.setSelection(false);
			this.confirm.setSelection(true);
			this.deploykey.setSelection(false);
		} else if (textfield_deploykey.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			this.login.setSelection(false);
			this.password.setSelection(false);
			this.confirm.setSelection(false);
			this.deploykey.setSelection(true);
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

	private DoubleRectangle textfield_login = new DoubleRectangle(155, 130, 257, 32);
	private DoubleRectangle textfield_pass = new DoubleRectangle(155, 176, textfield_login.width / 2 - 10, 32);
	private DoubleRectangle textfield_confirm = new DoubleRectangle(165 + textfield_login.width / 2, 176,
			textfield_login.width / 2 - 10, 32);
	private DoubleRectangle textfield_deploykey = new DoubleRectangle(155, 222, 257, 32);
	private DoubleRectangle button_create = new DoubleRectangle(211, 311, 128, 38);
	private DoubleRectangle button_back = new DoubleRectangle(33, 150, 82, 27);

	@Override
	public void render(Graphics2D g, int width, int height) {
		Graphics2D gcopy = (Graphics2D) g.create();
		MainUiUtility.drawBackground(g, width, height, (byte) 2);
		offsetx = width / 2 - squarewidth / 2;
		offsety = height / 2 - squareheight / 2;
		int relativemousex = mouseX - offsetx, relativemousey = mouseY - offsety;
		g.translate(offsetx, offsety);
		gcopy.translate(offsetx, offsety);
		// DRAWS THE ACCOUNT CREATION FACILITIES
		g.drawImage(Sprites.Res_Hud.createaccountframe.image(), 0, 0, null);

		gcopy.setColor(Color.BLACK);
		gcopy.setFont(Res.getFont().deriveFont(26f).deriveFont(Font.BOLD));
		gcopy.drawString(this.titleM.toString(), 195, 112);
		gcopy.setColor(new Color(242, 145, 0));
		gcopy.drawString(this.titleM.toString(), 197, 113);

		gcopy.setColor(Color.RED);
		gcopy.setFont(Res.getFont().deriveFont(22f));
		gcopy.drawString(errormessage, 80, 295);
		gcopy.setColor(new Color(4, 142, 52));
		gcopy.drawString(assessmessage, 185, 355);

		// Draws the textfield backgrounds
		g.setColor(new Color(66, 122, 255));
		g.fillRect((int) textfield_login.x, (int) textfield_login.y, (int) textfield_login.width,
				(int) textfield_login.height);
		g.fillRect((int) textfield_pass.x, (int) textfield_pass.y, (int) textfield_pass.width,
				(int) textfield_pass.height);
		g.fillRect((int) textfield_confirm.x, (int) textfield_confirm.y, (int) textfield_confirm.width,
				(int) textfield_confirm.height);
		g.fillRect((int) textfield_deploykey.x, (int) textfield_deploykey.y, (int) textfield_deploykey.width,
				(int) textfield_deploykey.height);

		gcopy.setColor(new Color(242, 145, 0));
		gcopy.setFont(Res.getFont().deriveFont(20f).deriveFont(Font.BOLD));
		gcopy.drawString(this.usernameM.toString(), (int) textfield_login.x, (int) textfield_login.y);
		gcopy.drawString(this.passwordM.toString(), (int) textfield_pass.x, (int) textfield_pass.y);
		gcopy.drawString(this.confirmM.toString(), (int) textfield_confirm.x, (int) textfield_confirm.y);
		gcopy.drawString(this.deployM.toString(), (int) textfield_deploykey.x, (int) textfield_deploykey.y);

		// Draws the textfields elements
		g.translate(textfield_login.x + 10, textfield_login.y);
		this.login.render(g, (int) textfield_login.width - 20, (int) textfield_login.height - 15);
		g.translate(-textfield_login.x - 10, -textfield_login.y);

		g.translate(textfield_pass.x + 10, textfield_pass.y);
		this.password.render(g, (int) textfield_pass.width - 20, (int) textfield_pass.height - 15);
		g.translate(-textfield_pass.x - 10, -textfield_pass.y);

		g.translate(textfield_confirm.x + 10, textfield_confirm.y);
		this.confirm.render(g, (int) textfield_confirm.width - 20, (int) textfield_confirm.height - 15);
		g.translate(-textfield_confirm.x - 10, -textfield_confirm.y);

		g.translate(textfield_deploykey.x + 10, textfield_deploykey.y);
		this.deploykey.render(g, (int) textfield_deploykey.width - 20, (int) textfield_deploykey.height - 15);
		g.translate(-textfield_deploykey.x - 10, -textfield_deploykey.y);

		// Draws the buttons
		gcopy.setFont(Res.getFont().deriveFont(24f).deriveFont(Font.BOLD));
		Color buttoncolor = new Color(124, 163, 255), selectedbuttoncolor = new Color(183, 222, 255);
		gcopy.setColor((button_create.isInside(new Position(relativemousex, relativemousey))) ? selectedbuttoncolor
				: buttoncolor);
		gcopy.fillRect((int) button_create.x, (int) button_create.y, (int) button_create.width,
				(int) button_create.height);
		gcopy.setColor(new Color(30, 15, 210));
		gcopy.drawString(this.createM.toString(), (int) button_create.x + 6, (int) button_create.y + 25);

		gcopy.setColor((button_back.isInside(new Position(relativemousex, relativemousey))) ? selectedbuttoncolor
				: buttoncolor);
		gcopy.fillRect((int) button_back.x, (int) button_back.y, (int) button_back.width, (int) button_back.height);
		gcopy.setColor(new Color(30, 15, 210));
		gcopy.drawString(this.backM.toString(), (int) button_back.x + 10, (int) button_back.y + 18);

		// REVERT GRAPHICS
		gcopy.dispose();
		g.translate(-offsetx, -offsety);
	}

	@Override
	public void update() {
		this.login.update();
		this.password.update();
		this.confirm.update();
		this.deploykey.update();
		if (firstupdate) {
			firstupdate = false;
			this.password.setSelection(false);
			this.confirm.setSelection(false);
			this.deploykey.setSelection(false);
		}
	}

	private boolean firstupdate = true;

	/** Sends the account creation payload to the server after proper encryption. */
	private void sendAccountcreationMessage() {
		try {
			String message = "";
			// local hash that is somewhat safer to keep in memory. It doesn't
			// make sense securizing this variable as it's value is identical to
			// the user's local storage.
			String localhash = Encryption.clientHash(this.password.getContent(), this.login.getContent(),
					Encryption.HASHSALTTYPE_CLIENT);
			JsonObject mess = new JsonObject().add("action", "createaccount").add("name", this.login.getContent())
					.add("deploykey", this.deploykey.getContent()).add("passhash",
							Encryption.clientHash(localhash, this.login.getContent(), Encryption.HASHSALTTYPE_SERVER));
			message = mess.toString();
			Persistence.socketendpoint.sendMessage(message);
		} catch (Exception e) {
			Logger.e(
					"Could not send the create account information payload because... HOLY SHIT THIS SHOULD NOT BE HAPPENING");
			Encryption.death256message();
			System.exit(666);
		}
	}

	/** Method called when recieving a logininfo payload. */
	public void servercall(String id) {
		switch (id) {
			default:
				errormessage = new Message(id).toString();
				assessmessage = "";
				break;
			case "ui.login.accountcreated":
				errormessage = "";
				assessmessage = new Message(id).toString();
				Persistence.stateManager = new AccountCreatedMainState(password.getContent(), login.getContent());
				break;
		}
	}

}
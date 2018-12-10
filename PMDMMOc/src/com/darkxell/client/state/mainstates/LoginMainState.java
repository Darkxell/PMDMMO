package com.darkxell.client.state.mainstates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.launchable.crypto.Encryption;
import com.darkxell.client.mechanics.chat.CustomTextfield;
import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.client.state.OpenningState;
import com.darkxell.client.state.PlayerLoadingState;
import com.darkxell.client.state.PlayerLoadingState.PlayerLoadingEndListener;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.TransitionState;
import com.darkxell.client.state.quiz.PersonalityQuizDialog;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.client.ui.MainUiUtility;
import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.common.player.Player;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.Position;
import com.darkxell.common.util.Util;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.JsonObject;

public class LoginMainState extends StateManager {

	private static final boolean DEBUGALLOWED = true;

	private CustomTextfield login = new CustomTextfield();
	private CustomTextfield password = new CustomTextfield().setObfuscated();
	private String localsalt = "";
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

	/** A message displayed above the login button. */
	private String message = "";
	private Color messcolor = new Color(4, 142, 52);

	public LoginMainState() {
		super();
	}

	public LoginMainState(String message, String prefilledname, String prefilledpass) {
		this();
		if (prefilledname != null) {
			login.clear();
			login.insertString(prefilledname);
		}
		if (prefilledpass != null) {
			password.clear();
			password.insertString(prefilledpass);
		}
		this.message = message;
	}

	@Override
	public void onKeyPressed(KeyEvent e, Key key) {
		login.onKeyPressed(e);
		password.onKeyPressed(e);
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (Persistance.socketendpoint.connectionStatus() == GameSocketEndpoint.CONNECTED)
				launchOnlineSend();
		} else if (e.getKeyCode() == KeyEvent.VK_UP && this.password.isSelected()) {
			this.login.setSelection(true);
			this.password.setSelection(false);
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN && this.login.isSelected()) {
			this.login.setSelection(false);
			this.password.setSelection(true);
		}
	}

	@Override
	public void onKeyReleased(KeyEvent e, Key key) {

	}

	@Override
	public void onKeyTyped(KeyEvent e) {
		login.onKeyTyped(e);
		password.onKeyTyped(e);
	}

	@Override
	public void onMouseClick(int x, int y) {
		// Button clicks
		if (button_createaccount.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			if (Persistance.socketendpoint.connectionStatus() == GameSocketEndpoint.CONNECTED)
				Persistance.stateManager = new AccountCreationState();
		} else if (button_login.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			if (Persistance.socketendpoint.connectionStatus() == GameSocketEndpoint.CONNECTED)
				launchOnlineSend();
		} else if (button_offline.isInside(new Position(mouseX - offsetx, mouseY - offsety)) && DEBUGALLOWED) {
			launchOffline();
		}
		// Textfield focus
		if (textfield_login.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			this.login.setSelection(true);
			this.password.setSelection(false);
		} else if (textfield_pass.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
			this.login.setSelection(false);
			this.password.setSelection(true);
		} else {
			this.login.setSelection(false);
			this.password.setSelection(false);
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

	private DoubleRectangle textfield_login = new DoubleRectangle(37, 174, 206, 32);
	private DoubleRectangle textfield_pass = new DoubleRectangle(37, 225, 206, 32);
	private DoubleRectangle button_login = new DoubleRectangle(72, 286, 110, 33);
	private DoubleRectangle button_createaccount = new DoubleRectangle(324, 196, 139, 33);
	private DoubleRectangle button_offline = new DoubleRectangle(406, 317, 82, 20);

	@Override
	public void render(Graphics2D g, int width, int height) {
		Graphics2D gcopy = (Graphics2D) g.create();
		MainUiUtility.drawBackground(g, width, height, (byte) 1);
		offsetx = width / 2 - squarewidth / 2;
		offsety = height / 2 - squareheight / 2;
		int relativemousex = mouseX - offsetx, relativemousey = mouseY - offsety;
		g.translate(offsetx, offsety);
		gcopy.translate(offsetx, offsety);
		// DRAWS THE LOGIN FACILITIES
		g.drawImage(Sprites.Res_Hud.loginframe.image(), 0, 0, null);

		gcopy.setColor(Color.BLACK);
		gcopy.setFont(Res.getFont().deriveFont(26f).deriveFont(Font.BOLD));
		gcopy.drawString("Log in with your account", 36, 131);
		gcopy.drawString("Create account", 334, 131);
		gcopy.setColor(new Color(242, 145, 0));
		gcopy.drawString("Log in with your account", 38, 132);
		gcopy.drawString("Create account", 336, 132);

		// Draws the textfield backgrounds
		g.setColor(new Color(66, 122, 255));
		g.fillRect((int) textfield_login.x, (int) textfield_login.y, (int) textfield_login.width,
				(int) textfield_login.height);
		g.fillRect((int) textfield_pass.x, (int) textfield_pass.y, (int) textfield_pass.width,
				(int) textfield_pass.height);

		gcopy.setColor(new Color(242, 145, 0));
		gcopy.setFont(Res.getFont().deriveFont(20f).deriveFont(Font.BOLD));
		gcopy.drawString("Login", (int) textfield_login.x, (int) textfield_login.y);
		gcopy.drawString("Password", (int) textfield_pass.x, (int) textfield_pass.y);

		// Draws the textfields

		g.translate(textfield_login.x + 10, textfield_login.y);
		this.login.render(g, (int) textfield_login.width - 20, (int) textfield_login.height - 15);
		g.translate(-textfield_login.x - 10, -textfield_login.y);

		g.translate(textfield_pass.x + 10, textfield_pass.y);
		this.password.render(g, (int) textfield_pass.width - 20, (int) textfield_pass.height - 15);
		g.translate(-textfield_pass.x - 10, -textfield_pass.y);

		Color buttoncolor = new Color(124, 163, 255), selectedbuttoncolor = new Color(183, 222, 255);
		g.setColor((button_login.isInside(new Position(relativemousex, relativemousey))) ? selectedbuttoncolor
				: buttoncolor);
		if (Persistance.socketendpoint.connectionStatus() != GameSocketEndpoint.CONNECTED) {
			g.setColor(Color.RED);
			g.drawString("Not connected to servers", (int) button_login.x - 10, (int) button_login.y + 60);
			g.setColor(new Color(120, 120, 180));
		}
		g.fillRect((int) button_login.x, (int) button_login.y, (int) button_login.width, (int) button_login.height);
		g.setColor(Color.DARK_GRAY);
		g.drawString(message, 39, 276);
		g.setColor(messcolor);
		g.drawString(message, 40, 275);
		// Displays the buttons
		gcopy.setFont(Res.getFont().deriveFont(24f).deriveFont(Font.BOLD));
		gcopy.setColor(new Color(30, 15, 210));
		gcopy.drawString("LOGIN", (int) button_login.x + 25, (int) button_login.y + 20);
		g.setColor((button_createaccount.isInside(new Position(relativemousex, relativemousey))) ? selectedbuttoncolor
				: buttoncolor);
		if (Persistance.socketendpoint.connectionStatus() != GameSocketEndpoint.CONNECTED)
			g.setColor(new Color(120, 120, 180));
		g.fillRect((int) button_createaccount.x, (int) button_createaccount.y, (int) button_createaccount.width,
				(int) button_createaccount.height);
		gcopy.setColor(new Color(30, 15, 210));
		gcopy.drawString("Create account", (int) button_createaccount.x + 15, (int) button_createaccount.y + 20);
		g.setColor((button_offline.isInside(new Position(relativemousex, relativemousey))) ? selectedbuttoncolor
				: buttoncolor);
		if (!DEBUGALLOWED)
			g.setColor(new Color(120, 120, 180));
		g.fillRect((int) button_offline.x, (int) button_offline.y, (int) button_offline.width,
				(int) button_offline.height);
		gcopy.setColor(new Color(30, 15, 210));
		gcopy.drawString("Debug", (int) button_offline.x + 5, (int) button_offline.y + 14);

		// REVERT GRAPHICS
		gcopy.dispose();
		g.translate(-offsetx, -offsety);
	}

	@Override
	public void update() {
		this.login.update();
		this.password.update();
		if (firstupdate) {
			firstupdate = false;
			this.password.setSelection(false);
			this.sendSaltReset();
			if (this.login.getContent().equals(""))
				this.login.insertString(ClientSettings.getSetting(ClientSettings.LOGIN));
		}
	}

	private boolean firstupdate = true;

	private void sendLogin() {
		String localhash;
		try {
			localhash = Encryption.clientHash(this.password.getContent(), this.login.getContent(),
					Encryption.HASHSALTTYPE_CLIENT);
			String serverhash = Encryption.clientHash(localhash, this.login.getContent(),
					Encryption.HASHSALTTYPE_SERVER);
			String loginhash = Encryption.clientHash(serverhash, localsalt, Encryption.HASHSALTTYPE_LOGIN);
			JsonObject message = new JsonObject().add("action", "login").add("name", this.login.getContent())
					.add("passhash", loginhash);
			Persistance.socketendpoint.sendMessage(message.toString());
		} catch (Exception e) {
			Encryption.death256message();
			System.exit(666);
		}
	}

	private void sendSaltReset() {
		JsonObject message = new JsonObject().add("action", "saltreset");
		Persistance.socketendpoint.sendMessage(message.toString());
	}

	public void setSalt(String salt) {
		this.localsalt = salt;
		Logger.d("Recieved salt for this loginState : " + salt);
	}

	/** Called when the player clicks the login button. */
	private void launchOnlineSend() {
		ClientSettings.setSetting(ClientSettings.LOGIN, this.login.getContent());
		this.sendLogin();
		Logger.i("Sent login infos to the server, awaiting response...");
	}

	/**
	 * Called when the server responds positively to the login attempt made by
	 * <code>launchOnlineSend()</code>
	 */
	public void launchOnlineOnRecieve(JsonObject pl) {
		Logger.i("Recieved connection informations, player is connected to the server.");
		DBPlayer playerdata = new DBPlayer();
		playerdata.read(pl);
		Persistance.player = new Player(playerdata);
		Persistance.stateManager = new PrincipalMainState();
		if (Persistance.player.storyPosition() == 0)
			Persistance.stateManager.setState(new TransitionState(null, new PersonalityQuizDialog().getLoadingState()));
		else
			Persistance.stateManager
					.setState(new PlayerLoadingState(Persistance.player.getData().id, new PlayerLoadingEndListener() {
					}));
		((PrincipalMainState) Persistance.stateManager).randomizeBackground();
	}

	/** Called when the user presses the play offline debug mode */
	private void launchOffline() {
		// Set placeholder data to fake an account creation
		Persistance.player = Util.createDefaultPlayer();
		Persistance.currentplayer.setPlayer(Persistance.player);

		// Go to the game state
		Persistance.stateManager = new PrincipalMainState();
		// ((PrincipalMainState) Persistance.stateManager).setState(new
		// PersonalityQuizState());
		((PrincipalMainState) Persistance.stateManager).setState(new OpenningState());
		((PrincipalMainState) Persistance.stateManager).randomizeBackground();
	}

	/** Method called when recieving a logininfo payload. */
	public void servercall(String id) {
		if (id.equals("ui.login.loginunmatched") || id.equals("ui.login.loginunknown")
				|| id.equals("ui.login.salterror")) {
			sendSaltReset();
			this.messcolor = Color.RED;
			this.message = new Message(id).toString();
		}
	}

}

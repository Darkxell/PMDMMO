package com.darkxell.client.state.mainstates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.Encryption;
import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.chat.CustomTextfield;
import com.darkxell.client.resources.images.others.Hud;
import com.darkxell.client.state.OpenningState;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.ui.MainUiUtility;
import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.common.item.ItemID;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.Position;
import com.eclipsesource.json.JsonObject;

public class LoginMainState extends StateManager {

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

	@Override
	public void onKeyPressed(KeyEvent e, short key) {
		login.onKeyPressed(e);
		password.onKeyPressed(e);
	}

	@Override
	public void onKeyReleased(KeyEvent e, short key) {

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
		} else if (button_offline.isInside(new Position(mouseX - offsetx, mouseY - offsety))) {
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
		MainUiUtility.drawBackground(g, width, height, (byte) 1);
		offsetx = width / 2 - squarewidth / 2;
		offsety = height / 2 - squareheight / 2;
		int relativemousex = mouseX - offsetx, relativemousey = mouseY - offsety;
		g.translate(offsetx, offsety);
		// DRAWS THE LOGIN FACILITIES
		g.drawImage(Hud.loginframe, 0, 0, null);

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
		g.setColor(Color.BLACK);
		g.drawString("LOGIN", (int) button_login.x + 25, (int) button_login.y + 20);
		g.setColor((button_createaccount.isInside(new Position(relativemousex, relativemousey))) ? selectedbuttoncolor
				: buttoncolor);
		if (Persistance.socketendpoint.connectionStatus() != GameSocketEndpoint.CONNECTED)
			g.setColor(new Color(120, 120, 180));
		g.fillRect((int) button_createaccount.x, (int) button_createaccount.y, (int) button_createaccount.width,
				(int) button_createaccount.height);
		g.setColor(Color.BLACK);
		g.drawString("CREATE ACCOUNT", (int) button_createaccount.x + 15, (int) button_createaccount.y + 20);
		g.setColor((button_offline.isInside(new Position(relativemousex, relativemousey))) ? selectedbuttoncolor
				: buttoncolor);
		g.fillRect((int) button_offline.x, (int) button_offline.y, (int) button_offline.width,
				(int) button_offline.height);
		g.setColor(Color.BLACK);
		g.drawString("Offline debug", (int) button_offline.x + 5, (int) button_offline.y + 14);

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
			this.sendSaltReset();
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
		((PrincipalMainState) Persistance.stateManager).setState(new OpenningState());
		((PrincipalMainState) Persistance.stateManager).randomizeBackground();
	}

	/** Called when the user presses the play offline debug mode */
	private void launchOffline() {
		// Set placeholder data to fake an account creation
		Persistance.player = new Player("Offline debug account name",
				PokemonRegistry.find(6).generate(new Random(), 1));
		Persistance.player.setStoryPosition(1);
		Persistance.player.addAlly(PokemonRegistry.find(252).generate(new Random(), 1, 1));
		Persistance.player.addAlly(PokemonRegistry.find(255).generate(new Random(), 1));
		Persistance.player.getTeamLeader().setItem(new ItemStack(ItemID.XRaySpecs));
		Persistance.player.getTeamLeader().setMove(3, new LearnedMove(801));
		Persistance.currentplayer.setPlayer(Persistance.player);

		// Go to the game state
		Persistance.stateManager = new PrincipalMainState();
		// ((PrincipalMainState) Persistance.stateManager).setState(new
		// PersonalityQuizState());
		((PrincipalMainState) Persistance.stateManager).setState(new OpenningState());
		((PrincipalMainState) Persistance.stateManager).randomizeBackground();
	}

}

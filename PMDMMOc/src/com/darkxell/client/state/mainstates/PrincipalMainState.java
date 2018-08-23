package com.darkxell.client.state.mainstates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.AnimationTicker;
import com.darkxell.client.renderers.TeamInfoRenderer;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.TransitionState;
import com.darkxell.client.ui.Keys;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.client.ui.MainUiUtility;
import com.darkxell.common.util.language.Message;

/**
 * The principal state of the game. Displays the game screen, minimap, chat
 * window and pokemons informations.
 */
public class PrincipalMainState extends StateManager {

	// ATTRIBUTES

	/** The current game state. */
	private AbstractState currentState;

	private BufferedImage internalBuffer;
	public static final int displayWidth = (int) (256 * 1.6);
	public static final int displayHeight = (int) (192 * 1.6);

	public byte backgroundID = 1;

	private int gamewidth;
	private int gameheight;
	private int gamex;
	private int gamey;
	private int mapsize;
	private int mapx;
	private int mapy;
	private int chatwidth;
	private int chatheight;
	private int chatx;
	private int chaty;
	private int teamwidth;

	private boolean isChatFocused = false;
	private boolean isGameFocused = true;

	// KEY AND MOUSE EVENTS
	@Override
	public void onKeyPressed(KeyEvent e, Key key) {
		if (this.currentState != null && isGameFocused)
			this.currentState.onKeyPressed(key);
		if (e.getKeyCode() == KeyEvent.VK_ENTER && isChatFocused)
			Persistance.chatbox.send();
		if (isChatFocused)
			Persistance.chatbox.textfield.onKeyPressed(e);
		if (e.getKeyCode() == KeyEvent.VK_F12 && e.isShiftDown())
			Persistance.debugdisplaymode = !Persistance.debugdisplaymode;
		if (e.getKeyCode() == KeyEvent.VK_F11 && e.isShiftDown())
			Persistance.debugwiresharkmode = !Persistance.debugwiresharkmode;
		if (e.getKeyCode() == KeyEvent.VK_F10 && e.isShiftDown())
			Persistance.debugcommunicationmode = !Persistance.debugcommunicationmode;
	}

	@Override
	public void onKeyReleased(KeyEvent e, Key key) {
		if (this.currentState != null && isGameFocused)
			this.currentState.onKeyReleased(key);
	}

	@Override
	public void onKeyTyped(KeyEvent e) {
		if (isChatFocused)
			Persistance.chatbox.textfield.onKeyTyped(e);
	}

	@Override
	public void onMouseClick(int x, int y) {
		if (this.currentState != null && isGameFocused) {
			Point p = this.inGameLocation(x, y);
			if (p != null)
				this.currentState.onMouseClick(p.x, p.y);
		}
		if (x > gamex && x < gamex + gamewidth && y > gamey && y < gamey + gameheight) {
			isGameFocused = true;
			isChatFocused = false;
		} else if (x > chatx && x < chatx + chatwidth && y > chaty && y < chaty + chatheight) {
			isChatFocused = true;
			isGameFocused = false;
			Persistance.chatbox.onClick(x - chatx, y - chaty);
		}
	}

	@Override
	public void onMouseMove(int x, int y) {
		if (this.currentState != null && isGameFocused) {
			Point p = this.inGameLocation(x, y);
			if (p != null)
				this.currentState.onMouseMove(p.x, p.y);
		}
	}

	@Override
	public void onMouseRightClick(int x, int y) {
		if (this.currentState != null && isGameFocused) {
			Point p = this.inGameLocation(x, y);
			if (p != null)
				this.currentState.onMouseRightClick(p.x, p.y);
		}
	}

	private Point inGameLocation(int x, int y) {
		if (this.internalBuffer == null)
			return null;
		Point p = new Point((x - this.gamex) * this.internalBuffer.getWidth() / this.gamewidth,
				(y - this.gamey) * this.internalBuffer.getHeight() / this.gameheight);
		if (p.x < 0 || p.y < 0 || p.x > this.internalBuffer.getWidth() || p.y > this.internalBuffer.getHeight())
			return null;
		return p;
	}

	// RENDER AND UPDATE
	@Override
	public void render(Graphics2D g, int width, int height) {
		if (width == 0)
			return;
		if (internalBuffer == null)
			internalBuffer = new BufferedImage(displayWidth, displayHeight, BufferedImage.TYPE_INT_ARGB);

		// Displays the game on the buffer
		Graphics2D g2 = internalBuffer.createGraphics();
		g2.clearRect(0, 0, displayWidth, displayHeight);
		if (this.currentState != null)
			this.currentState.render(g2, displayWidth, displayHeight);
		if (!isGameFocused) {
			g2.setColor(new Color(0, 0, 0, 150));
			g2.fillRect(0, 0, displayWidth, displayHeight);
		}
		g2.dispose();

		// Calculates various values to draw the components to the window
		gamewidth = (int) (0.6 * height * displayWidth / displayHeight <= 0.6 * width
				? 0.6 * height * displayWidth / displayHeight : 0.6 * width);
		gameheight = gamewidth * displayHeight / displayWidth;
		gamex = (int) (width * 0.35);
		gamey = (int) (height * 0.35);
		mapsize = (int) (gamewidth / 2 >= height * 0.25 ? height * 0.25 : gamewidth / 2);
		mapx = (int) (width * 0.95) - mapsize < gamex + gamewidth - mapsize ? (int) (width * 0.95) - mapsize
				: gamex + gamewidth - mapsize;
		mapy = (int) (height * 0.05);
		chatwidth = (int) (width * 0.25);
		chatheight = (int) (height * 0.9);
		chatx = (int) (width * 0.05);
		chaty = (int) (height * 0.05);
		teamwidth = (int) (gamewidth - mapsize - width * 0.05);

		// Draws the background
		MainUiUtility.drawBackground(g, width, height, backgroundID);

		// Draw the outlines
		MainUiUtility.drawBoxOutline(g, gamex, mapy, teamwidth, mapsize);
		MainUiUtility.drawBoxOutline(g, gamex, gamey, gamewidth, gameheight);
		MainUiUtility.drawBoxOutline(g, mapx, mapy, mapsize, mapsize);
		MainUiUtility.drawBoxOutline(g, chatx, chaty, chatwidth, chatheight);

		// draws the game inside
		g.drawImage(internalBuffer, gamex, gamey, gamewidth, gameheight, null);
		if (Persistance.isCommunicating) {
			g.setColor(Palette.TRANSPARENT_GRAY);
			Message m = new Message("general.loading");
			int mxoff = 4, myoff = 2;
			int mwidth = TextRenderer.width(m) + mxoff * 2, mheight = TextRenderer.height() + myoff * 2;
			g.fillRect(gamex + gamewidth - mwidth, gamey, mwidth, mheight);
			TextRenderer.render(g, m, gamex + gamewidth - mwidth + mxoff, gamey + myoff);
		}

		// draws the chat inside
		g.translate(chatx, chaty);
		Shape clp = g.getClip();
		g.setClip(new Rectangle(0, 0, chatwidth, chatheight));
		Persistance.chatbox.render(g, chatwidth, chatheight, isChatFocused);
		g.setClip(clp);
		g.translate(-chatx, -chaty);

		// draws the map inside
		g.translate(mapx, mapy);
		clp = g.getClip();
		g.setClip(new Rectangle(0, 0, mapsize, mapsize));
		if (Persistance.displaymap == null) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, mapsize, mapsize);
		} else
			Persistance.displaymap.render(g, mapsize, mapsize);
		if (Persistance.stateManager.getCurrentState() instanceof TransitionState) {
			int alpha = ((TransitionState) Persistance.stateManager.getCurrentState()).minimapFading();
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, mapsize, mapsize);
		}
		g.setClip(clp);
		g.translate(-mapx, -mapy);

		// draws the team info
		g.translate(gamex, mapy);
		clp = g.getClip();
		g.setClip(new Rectangle(0, 0, teamwidth, mapsize));
		TeamInfoRenderer.render(g, teamwidth, mapsize);
		g.setClip(clp);
		g.translate(-gamex, -mapy);
	}

	@Override
	public synchronized void update() {
		AnimationTicker.instance.update(); // Update animations before to able post-update modifications.
		if (this.currentState != null)
			this.currentState.update();
		if (Persistance.displaymap != null)
			Persistance.displaymap.update();
		Keys.update();
	}

	// GETTERS,SETTERS AND UTILITY

	public void randomizeBackground() {
		this.backgroundID = (byte) ((new Random().nextInt(7) + 1) % 7);
	}

	public AbstractState getCurrentState() {
		return this.currentState;
	}

	@Override
	public void setState(AbstractState state) {
		if (state == this.currentState)
			return;
		if (this.currentState != null)
			this.currentState.onEnd();
		this.currentState = state;
		this.currentState.onStart();
	}

}

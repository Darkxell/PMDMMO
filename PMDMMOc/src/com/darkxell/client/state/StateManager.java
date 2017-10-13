package com.darkxell.client.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.AnimationTicker;
import com.darkxell.client.ui.MainUiUtility;

public class StateManager {

	// ATTRIBUTES

	private AbstractState currentState;

	private BufferedImage internalBuffer;
	private int displayWidth = (int) (256 * 1.6);
	private int displayHeight = (int) (192 * 1.6);

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

	private boolean isChatFocused = false;
	private boolean isGameFocused = true;

	// KEY AND MOUSE EVENTS

	public void onKeyPressed(KeyEvent e, short key) {
		if (this.currentState != null && isGameFocused)
			this.currentState.onKeyPressed(key);
		if (e.getKeyCode() == KeyEvent.VK_ENTER && isChatFocused)
			Persistance.chatbox.send();
		if (e.getKeyCode() == KeyEvent.VK_LEFT && isChatFocused)
			Persistance.chatbox.textfield.pressLeft();
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && isChatFocused)
			Persistance.chatbox.textfield.pressRight();
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && isChatFocused)
			Persistance.chatbox.textfield.pressDelete();
	}

	public void onKeyReleased(KeyEvent e, short key) {
		if (this.currentState != null && isGameFocused)
			this.currentState.onKeyReleased(key);
	}

	public void onKeyTyped(KeyEvent e) {
		if (e.getKeyChar() != '\b' && isChatFocused)
			Persistance.chatbox.textfield.insertString(e.getKeyChar() + "");
	}

	public void onMouseClick(int x, int y) {
		if (this.currentState != null && isGameFocused)
			this.currentState.onMouseClick(x, y);
		if (x > gamex && x < gamex + gamewidth && y > gamey && y < gamey + gameheight) {
			isGameFocused = true;
			isChatFocused = false;
		} else if (x > chatx && x < chatx + chatwidth && y > chaty && y < chaty + chatheight) {
			isChatFocused = true;
			isGameFocused = false;
			Persistance.chatbox.onClick(x - chatx, y - chaty);
		}
	}

	public void onMouseMove(int x, int y) {
		if (this.currentState != null && isGameFocused)
			this.currentState.onMouseMove(x, y);
	}

	public void onMouseRightClick(int x, int y) {
		if (this.currentState != null && isGameFocused)
			this.currentState.onMouseRightClick(x, y);
	}

	// RENDER AND UPDATE

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
		// Draws the background
		MainUiUtility.drawBackground(g, width, height, backgroundID);
		// Draw the outlines
		MainUiUtility.drawBoxOutline(g, gamex, gamey, gamewidth, gameheight);
		MainUiUtility.drawBoxOutline(g, mapx, mapy, mapsize, mapsize);
		MainUiUtility.drawBoxOutline(g, chatx, chaty, chatwidth, chatheight);
		// draws the game inside
		g.drawImage(internalBuffer, gamex, gamey, gamewidth, gameheight, null);
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
		Persistance.displaymap.render(g, mapsize, mapsize);
		g.setClip(clp);
		g.translate(-mapx, -mapy);
	}

	public synchronized void update() {
		if (this.currentState != null)
			this.currentState.update();
		AnimationTicker.instance.update();
		Persistance.displaymap.update();
	}

	// GETTERS,SETTERS AND UTILITY

	/**
	 * Sets the resolution of the internal display. Bigger means a zoomed out
	 * game. Defaults at [256*192]*2 (Official DS resolution * 2).
	 */
	public void setInternalDisplaySize(int w, int h) {
		displayHeight = h;
		displayWidth = w;
		internalBuffer = null;
	}

	public void randomizeBackground() {
		this.backgroundID = (byte) ((new Random().nextInt(7) + 1) % 7);
	}

	public AbstractState getCurrentState() {
		return this.currentState;
	}

	public void setState(AbstractState state) {
		if (state == this.currentState) return;
		if (this.currentState != null)
			this.currentState.onEnd();
		this.currentState = state;
		this.currentState.onStart();
	}

}

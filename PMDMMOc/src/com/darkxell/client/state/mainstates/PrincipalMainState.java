package com.darkxell.client.state.mainstates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.AnimationTicker;
import com.darkxell.client.graphics.TeamInfoRenderer;
import com.darkxell.client.graphics.TextRenderer;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.TransitionState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.client.ui.MainUiUtility;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.language.Message;

/** The principal state of the game. Displays the game screen, minimap, chat window and pokemons informations. */
public class PrincipalMainState extends StateManager
{

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
	public void onKeyPressed(KeyEvent e, Key key)
	{
		if (this.currentState != null && isGameFocused) this.currentState.onKeyPressed(key);
		if (e.getKeyCode() == KeyEvent.VK_ENTER && isChatFocused) Persistence.chatbox.send();
		if (isChatFocused) Persistence.chatbox.textfield.onKeyPressed(e);
		if (isChatFocused && e.getKeyCode() == KeyEvent.VK_UP) Persistence.chatbox.up();
		if (e.getKeyCode() == KeyEvent.VK_F12 && e.isShiftDown()) Persistence.debugdisplaymode = !Persistence.debugdisplaymode;
		if (e.getKeyCode() == KeyEvent.VK_F11 && e.isShiftDown()) Persistence.debugwiresharkmode = !Persistence.debugwiresharkmode;
		if (e.getKeyCode() == KeyEvent.VK_F10 && e.isShiftDown()) Persistence.debugcommunicationmode = !Persistence.debugcommunicationmode;
	}

	@Override
	public void onKeyReleased(KeyEvent e, Key key)
	{
		if (this.currentState != null && isGameFocused) this.currentState.onKeyReleased(key);
	}

	@Override
	public void onKeyTyped(KeyEvent e)
	{
		if (isChatFocused) Persistence.chatbox.textfield.onKeyTyped(e);
		if (isGameFocused) this.currentState.onKeyTyped(e);
	}

	@Override
	public void onMouseClick(int x, int y)
	{
		if (this.currentState != null && isGameFocused)
		{
			Point p = this.inGameLocation(x, y);
			if (p != null) this.currentState.onMouseClick(p.x, p.y);
		}
		if (x > gamex && x < gamex + gamewidth && y > gamey && y < gamey + gameheight)
		{
			isGameFocused = true;
			isChatFocused = false;
		} else if (x > chatx && x < chatx + chatwidth && y > chaty && y < chaty + chatheight)
		{
			isChatFocused = true;
			isGameFocused = false;
			Persistence.chatbox.onClick(x - chatx, y - chaty);
		}
	}

	@Override
	public void onMouseMove(int x, int y)
	{
		if (this.currentState != null && isGameFocused)
		{
			Point p = this.inGameLocation(x, y);
			if (p != null) this.currentState.onMouseMove(p.x, p.y);
		}
	}

	@Override
	public void onMouseRightClick(int x, int y)
	{
		if (this.currentState != null && isGameFocused)
		{
			Point p = this.inGameLocation(x, y);
			if (p != null) this.currentState.onMouseRightClick(p.x, p.y);
		}
	}

	private Point inGameLocation(int x, int y)
	{
		if (this.internalBuffer == null || this.gamewidth == 0 || this.gameheight == 0) return null;
		Point p = new Point((x - this.gamex) * this.internalBuffer.getWidth() / this.gamewidth,
				(y - this.gamey) * this.internalBuffer.getHeight() / this.gameheight);
		if (p.x < 0 || p.y < 0 || p.x > this.internalBuffer.getWidth() || p.y > this.internalBuffer.getHeight()) return null;
		return p;
	}

	// RENDER AND UPDATE
	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (width == 0) return;
		if (internalBuffer == null) internalBuffer = new BufferedImage(displayWidth, displayHeight, BufferedImage.TYPE_INT_ARGB);

		// Displays the game on the buffer
		Graphics2D g2 = internalBuffer.createGraphics();
		g2.clearRect(0, 0, displayWidth, displayHeight);
		if (this.currentState != null) this.currentState.render(g2, displayWidth, displayHeight);
		if (!isGameFocused)
		{
			g2.setColor(new Color(0, 0, 0, 150));
			g2.fillRect(0, 0, displayWidth, displayHeight);
		}
		g2.dispose();

		// Calculates various values to draw the components to the window
		double kappa = 0.68 * height * displayWidth / displayHeight;
		gamewidth = (int) (kappa <= 0.68 * width ? kappa : 0.68 * width);
		gameheight = gamewidth * displayHeight / displayWidth;
		gamex = (int) (width * 0.30);
		gamey = (int) (height * 0.30);
		mapsize = (int) (gamewidth / 2 >= height * 0.25 ? height * 0.25 : gamewidth / 2);
		mapx = (int) (width * 0.95) - mapsize < gamex + gamewidth - mapsize ? (int) (width * 0.95) - mapsize : gamex + gamewidth - mapsize;
		mapy = (int) (height * 0.02);
		chatwidth = (int) (width * 0.225);
		chatheight = (int) (height * 0.96);
		chatx = (int) (width * 0.02);
		chaty = (int) (height * 0.02);
		teamwidth = (int) (mapx - gamex - 40);

		// Draws the background
		MainUiUtility.drawBackground(g, width, height, backgroundID);

		// Draw the outlines
		MainUiUtility.drawBoxOutline(g, gamex, gamey, gamewidth, gameheight);
		MainUiUtility.drawBoxOutline(g, mapx, mapy, mapsize, mapsize);
		MainUiUtility.drawBoxOutline(g, chatx, chaty, chatwidth, chatheight);

		// draws the game inside
		g.drawImage(internalBuffer, gamex, gamey, gamewidth, gameheight, null);
		if (Persistence.isCommunicating)
		{
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
		Persistence.chatbox.render(g, chatwidth, chatheight, isChatFocused);
		g.setClip(clp);
		g.translate(-chatx, -chaty);

		// draws the map inside
		g.translate(mapx, mapy);
		clp = g.getClip();
		g.setClip(new Rectangle(0, 0, mapsize, mapsize));
		if (Persistence.displaymap == null)
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, mapsize, mapsize);
		} else Persistence.displaymap.render(g, mapsize, mapsize);
		if (Persistence.stateManager.getCurrentState() instanceof TransitionState)
		{
			int alpha = ((TransitionState) Persistence.stateManager.getCurrentState()).minimapFading();
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, mapsize, mapsize);
		}
		g.setClip(clp);
		g.translate(-mapx, -mapy);

		// draws the team info
		g.translate(gamex, mapy);
		clp = g.getClip();
		g.setClip(new Rectangle(0, 0, teamwidth, mapsize));
		Pair<Integer, Integer> size = TeamInfoRenderer.render(g, teamwidth, mapsize);
		g.setClip(clp);
		g.translate(-gamex, -mapy);
		if (size.first != 0 && size.second != 0) MainUiUtility.drawBoxOutline(g, gamex, mapy, size.first + 1, size.second + 1);
	}

	@Override
	public synchronized void update()
	{
		AnimationTicker.instance.update(); // Update animations before to able post-update modifications.
		if (this.currentState != null) this.currentState.update();
		if (Persistence.displaymap != null) Persistence.displaymap.update();
	}

	// GETTERS,SETTERS AND UTILITY

	public void randomizeBackground()
	{
		this.backgroundID = (byte) ((new Random().nextInt(7) + 1) % 7);
	}

	public AbstractState getCurrentState()
	{
		return this.currentState;
	}

	@Override
	public void setState(AbstractState state)
	{
		if (state == this.currentState) return;
		if (this.currentState != null) this.currentState.onEnd();
		this.currentState = state;
		this.currentState.onStart();
	}
	
	public boolean isChatFocused() {
		return this.isChatFocused;
	}

}

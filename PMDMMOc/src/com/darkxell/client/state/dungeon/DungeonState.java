package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.darkxell.client.renderers.DungeonPokemonRenderer;
import com.darkxell.client.renderers.FloorRenderer;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.util.Message;

/** The main state for Dungeon exploration. */
public class DungeonState extends AbstractState
{
	/** A substate for Dungeon exploration. */
	static abstract class DungeonSubState extends AbstractState
	{
		public final DungeonState parent;

		public DungeonSubState(DungeonState parent)
		{
			this.parent = parent;
		}

		@Override
		public boolean isMain()
		{
			return this.parent.isMain();
		}

	}

	public static final int MESSAGE_TIME = 60 * 6;

	public final ActionSelectionState actionSelectionState;
	/** The current location of the Player on the screen (centered). */
	Point camera;
	/** The current substate. */
	private DungeonSubState currentSubstate;
	/** The delay before using the new substate. */
	private int delay = 0;
	boolean diagonal = false, rotating = false;
	public final Floor floor;
	final FloorRenderer floorRenderer;
	/** The last width the messages window were calculated for. Set to -1 to force reloading. */
	private int lastWidth = -1;
	/** Lists the last 40 messages. */
	private LinkedList<Message> log;
	/** The currently displayed messages. */
	private LinkedList<Message> messages;
	/** The window to draw messages in. */
	private MenuWindow messagesWindow;
	private int messageTime = 0;
	public final Player player;

	public DungeonState(Floor floor)
	{
		this.floor = floor;
		this.floorRenderer = new FloorRenderer(this.floor);
		this.player = new Player(0, PokemonRegistry.find(1).generate(new Random(), 10));
		Point p = this.floor.getTeamSpawn();
		this.floor.tileAt(p.x, p.y).setPokemon(this.player.getDungeonPokemon());

		this.log = new LinkedList<Message>();
		this.messages = new LinkedList<Message>();

		this.camera = new Point(this.player.getDungeonPokemon().tile.x * AbstractDungeonTileset.TILE_SIZE, this.player.getDungeonPokemon().tile.y
				* AbstractDungeonTileset.TILE_SIZE);
		this.currentSubstate = this.actionSelectionState = new ActionSelectionState(this);
		this.currentSubstate.onStart();
	}

	private void drawMessages(Graphics2D g, int width, int height)
	{
		if (this.lastWidth != width) this.reloadMessages(width, height);

		this.messagesWindow.render(g, null, width, height);
		for (int i = 0; i < this.messages.size() && i < 3; ++i)
			TextRenderer.instance.render(g, this.messages.get(this.messages.size() - i - 1), this.messagesWindow.dimensions.x + 20,
					this.messagesWindow.dimensions.y + (5 + TextRenderer.CHAR_HEIGHT) * i + MenuWindow.MARGIN_Y + 3);
	}

	public void hideMessages()
	{
		this.messageTime = 0;
	}

	/** @return The last 40 messages that were displayed to the Player. */
	public Message[] log()
	{
		return this.log.toArray(new Message[this.log.size()]);
	}

	@Override
	public void onKeyPressed(short key)
	{
		if (key == Keys.KEY_DIAGONAL) this.diagonal = true;
		if (key == Keys.KEY_ROTATE) this.rotating = true;

		this.currentSubstate.onKeyPressed(key);

	}

	@Override
	public void onKeyReleased(short key)
	{
		if (key == Keys.KEY_DIAGONAL) this.diagonal = false;
		if (key == Keys.KEY_ROTATE) this.rotating = false;

		this.currentSubstate.onKeyReleased(key);
	}

	private void reloadMessages(int width, int height)
	{
		int w = width - 40;
		int h = w * 5 / 28;
		this.messagesWindow = new MenuWindow(new Rectangle((width - w) / 2, height - h - 20, w, h));

		ArrayList<String> toReturn = new ArrayList<String>();
		for (Message m : this.messages)
			toReturn.addAll(TextRenderer.instance.splitLines(m.toString(), w - 40));

		this.messages.clear();
		for (int i = 0; i < 6 && i < toReturn.size(); ++i)
			this.messages.add(new Message(toReturn.get(i), false));

		this.lastWidth = width;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		int x = this.camera.x - width / 2, y = this.camera.y - height / 2;

		g.translate(-x, -y);

		this.floorRenderer.drawFloor(g, x, y, width, height);
		if (this.isMain()) if (this.delay == 0 && this.rotating && this.currentSubstate == this.actionSelectionState) this.floorRenderer.drawGrid(g,
				this.player.getDungeonPokemon(), x, y, width, height);
		this.floorRenderer.drawEntities(g, x, y, width, height);

		if (this.delay == 0) this.currentSubstate.render(g, width, height);

		g.translate(x, y);
		if (this.messageTime > 0) this.drawMessages(g, width, height);
	}

	public void setSubstate(DungeonSubState substate)
	{
		this.setSubstate(substate, 0);
	}

	/** @param substate - The new substate to use.
	 * @param delay - The delay before using that substate. */
	void setSubstate(DungeonSubState substate, int delay)
	{
		this.currentSubstate.onEnd();
		this.currentSubstate = substate;
		if (delay == 0) this.currentSubstate.onStart();
		this.delay = delay;
	}

	/** Shows a message to the player. */
	public void showMessage(Message message)
	{
		message.addReplacement("<player>", this.player.getPokemon().getNickname());
		this.log.add(message);
		this.messages.add(message);
		if (this.log.size() > 40) this.log.poll();
		if (this.messages.size() > 3) this.messages.poll();
		this.messageTime = MESSAGE_TIME;
		this.lastWidth = -1;
	}

	@Override
	public void update()
	{
		DungeonPokemonRenderer.instance.update();

		if (this.messageTime > 0)
		{
			if (this.messageTime == 1) this.messages.clear();
			--this.messageTime;
		}

		if (this.delay > 1) --this.delay;
		else if (this.delay == 1)
		{
			this.currentSubstate.onStart();
			--this.delay;
		} else this.currentSubstate.update();
	}

}

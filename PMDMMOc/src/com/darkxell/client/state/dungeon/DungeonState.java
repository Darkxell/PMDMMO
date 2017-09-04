package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

import com.darkxell.client.renderers.DungeonPokemonRenderer;
import com.darkxell.client.renderers.FloorRenderer;
import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.state.AbstractState;
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
	private LinkedList<Message> log;
	public final Player player;

	public DungeonState(Floor floor)
	{
		this.floor = floor;
		this.floorRenderer = new FloorRenderer(this.floor);
		this.player = new Player(0, PokemonRegistry.find(1).generate(new Random(), 10));
		Point p = this.floor.getTeamSpawn();
		this.floor.tileAt(p.x, p.y).setPokemon(this.player.getDungeonPokemon());

		this.log = new LinkedList<Message>();

		this.camera = new Point(this.player.getDungeonPokemon().tile.x * AbstractDungeonTileset.TILE_SIZE, this.player.getDungeonPokemon().tile.y
				* AbstractDungeonTileset.TILE_SIZE);
		this.currentSubstate = this.actionSelectionState = new ActionSelectionState(this);
		this.currentSubstate.onStart();
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
		System.out.println(message);
		this.log.add(message);
		if (this.log.size() > 40) this.log.poll();
	}

	@Override
	public void update()
	{
		DungeonPokemonRenderer.instance.update();
		if (this.delay > 1) --this.delay;
		else if (this.delay == 1)
		{
			this.currentSubstate.onStart();
			--this.delay;
		} else this.currentSubstate.update();
	}

}

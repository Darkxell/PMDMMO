package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.function.Predicate;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.renderers.DungeonPokemonRenderer;
import com.darkxell.client.renderers.FloorRenderer;
import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.GameUtil;
import com.darkxell.common.util.Logger;

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
	boolean diagonal = false, rotating = false;
	final FloorRenderer floorRenderer;
	public final DungeonLogger logger;

	public DungeonState()
	{
		this.floorRenderer = new FloorRenderer(DungeonPersistance.floor);
		Point p = DungeonPersistance.floor.teamSpawn;
		DungeonPersistance.floor.tileAt(p.x, p.y).setPokemon(DungeonPersistance.player.getDungeonPokemon());
		this.placeAllies();

		this.logger = new DungeonLogger(this);
		this.camera = new Point(DungeonPersistance.player.getDungeonPokemon().tile.x * AbstractDungeonTileset.TILE_SIZE,
				DungeonPersistance.player.getDungeonPokemon().tile.y * AbstractDungeonTileset.TILE_SIZE);
		this.currentSubstate = this.actionSelectionState = new ActionSelectionState(this);
		this.currentSubstate.onStart();
	}

	@Override
	public void onEnd()
	{
		super.onEnd();
		this.logger.hideMessages();
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
	public void onStart()
	{
		super.onStart();
		Launcher.soundmanager.setBackgroundMusic(SoundsHolder.getSong("07 Tiny Woods.mp3"));
	}

	private void placeAllies()
	{
		ArrayList<Tile> candidates = new ArrayList<Tile>();
		Tile initial = DungeonPersistance.player.getDungeonPokemon().tile;
		candidates.add(initial.adjacentTile(GameUtil.WEST));
		candidates.add(initial.adjacentTile(GameUtil.EAST));
		candidates.add(initial.adjacentTile(GameUtil.SOUTH));
		candidates.add(initial.adjacentTile(GameUtil.NORTH));
		candidates.add(initial.adjacentTile(GameUtil.NORTHWEST));
		candidates.add(initial.adjacentTile(GameUtil.NORTHEAST));
		candidates.add(initial.adjacentTile(GameUtil.SOUTHWEST));
		candidates.add(initial.adjacentTile(GameUtil.SOUTHEAST));
		candidates.removeIf(new Predicate<Tile>()
		{
			@Override
			public boolean test(Tile t)
			{
				return t.getPokemon() != null || t.type() == TileType.WALL || t.type() == TileType.WATER || t.type() == TileType.LAVA
						|| t.type() == TileType.AIR;
			}
		});

		for (DungeonPokemon p : DungeonPersistance.player.getDungeonTeam())
		{
			if (p == DungeonPersistance.player.getDungeonPokemon()) continue;
			if (candidates.size() == 0)
			{
				Logger.e("DungeonState.placeAllies() @124 : Could not find a spawn location for ally " + p.pokemon.getNickname() + "!");
				continue;
			}
			candidates.get(0).setPokemon(p);
			candidates.remove(0);
		}
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		int x = this.camera.x - width / 2, y = this.camera.y - height / 2;

		g.translate(-x, -y);

		this.floorRenderer.drawFloor(g, x, y, width, height);
		if (this.isMain()) if (this.rotating && this.currentSubstate == this.actionSelectionState) this.floorRenderer.drawGrid(g,
				DungeonPersistance.player.getDungeonPokemon(), x, y, width, height);
		this.floorRenderer.drawEntities(g, x, y, width, height);

		this.currentSubstate.render(g, width, height);

		g.translate(x, y);
		this.logger.render(g, width, height);
	}

	/** @param substate - The new substate to use. */
	public void setSubstate(DungeonSubState substate)
	{
		this.currentSubstate.onEnd();
		this.currentSubstate = substate;
		this.currentSubstate.onStart();
	}

	@Override
	public void update()
	{
		DungeonPokemonRenderer.instance.update();
		this.logger.update();
		this.currentSubstate.update();
	}

}

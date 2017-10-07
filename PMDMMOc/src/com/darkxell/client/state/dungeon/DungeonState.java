package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.function.Predicate;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.DungeonPokemonRenderer;
import com.darkxell.client.renderers.FloorRenderer;
import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Directions;
import com.darkxell.common.util.Logger;

/** The main state for Dungeon exploration. */
public class DungeonState extends AbstractState {
	/** A substate for Dungeon exploration. */
	static abstract class DungeonSubState extends AbstractState {
		public final DungeonState parent;

		public DungeonSubState(DungeonState parent) {
			this.parent = parent;
		}

		@Override
		public boolean isMain() {
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

	public DungeonState() {
		this.floorRenderer = new FloorRenderer(Persistance.floor);
		this.placeTeam();

		this.logger = new DungeonLogger(this);
		this.camera = new Point(Persistance.player.getDungeonPokemon().tile.x * AbstractDungeonTileset.TILE_SIZE,
				Persistance.player.getDungeonPokemon().tile.y * AbstractDungeonTileset.TILE_SIZE);
		this.currentSubstate = this.actionSelectionState = new ActionSelectionState(this);
		this.currentSubstate.onStart();
	}

	@Override
	public void onEnd() {
		super.onEnd();
		this.logger.hideMessages();
	}

	@Override
	public void onKeyPressed(short key) {
		if (key == Keys.KEY_DIAGONAL)
			this.diagonal = true;
		if (key == Keys.KEY_ROTATE)
			this.rotating = true;

		this.currentSubstate.onKeyPressed(key);

	}

	@Override
	public void onKeyReleased(short key) {
		if (key == Keys.KEY_DIAGONAL)
			this.diagonal = false;
		if (key == Keys.KEY_ROTATE)
			this.rotating = false;

		this.currentSubstate.onKeyReleased(key);
	}

	@Override
	public void onStart() {
		super.onStart();
		Persistance.soundmanager.setBackgroundMusic(SoundsHolder.getSong("07 Tiny Woods.mp3"));
	}

	private void placeTeam() {
		Point spawn = Persistance.floor.teamSpawn;
		Persistance.floor.tileAt(spawn.x, spawn.y).setPokemon(Persistance.player.getDungeonPokemon());
		Persistance.dungeon.insertActor(Persistance.player.getDungeonPokemon(), 0);

		ArrayList<Tile> candidates = new ArrayList<Tile>();
		Tile initial = Persistance.player.getDungeonPokemon().tile;
		candidates.add(initial.adjacentTile(Directions.WEST));
		candidates.add(initial.adjacentTile(Directions.EAST));
		candidates.add(initial.adjacentTile(Directions.SOUTH));
		candidates.add(initial.adjacentTile(Directions.NORTH));
		candidates.add(initial.adjacentTile(Directions.NORTHWEST));
		candidates.add(initial.adjacentTile(Directions.NORTHEAST));
		candidates.add(initial.adjacentTile(Directions.SOUTHWEST));
		candidates.add(initial.adjacentTile(Directions.SOUTHEAST));
		candidates.removeIf(new Predicate<Tile>() {
			@Override
			public boolean test(Tile t) {
				return t.getPokemon() != null || t.type() == TileType.WALL || t.type() == TileType.WATER
						|| t.type() == TileType.LAVA || t.type() == TileType.AIR;
			}
		});

		for (DungeonPokemon p : Persistance.player.getDungeonTeam()) {
			if (p == Persistance.player.getDungeonPokemon())
				continue;
			if (candidates.size() == 0) {
				Logger.e("DungeonState.placeAllies() @124 : Could not find a spawn location for ally "
						+ p.pokemon.getNickname() + "!");
				continue;
			}
			Persistance.floor.tileAt(candidates.get(0).x, candidates.get(0).y).setPokemon(p);
			Persistance.dungeon.insertActor(p, 1);
			candidates.remove(0);
		}
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		int x = this.camera.x - width / 2, y = this.camera.y - height / 2;

		g.translate(-x, -y);

		this.floorRenderer.drawFloor(g, x, y, width, height);
		if (this.isMain())
			if (this.rotating && this.currentSubstate == this.actionSelectionState)
				this.floorRenderer.drawGrid(g, Persistance.player.getDungeonPokemon(), x, y, width, height);
		this.floorRenderer.drawEntities(g, x, y, width, height);

		this.currentSubstate.render(g, width, height);

		g.translate(x, y);
		this.logger.render(g, width, height);
	}

	/**
	 * @param substate
	 *            - The new substate to use.
	 */
	public void setSubstate(DungeonSubState substate) {
		this.currentSubstate.onEnd();
		this.currentSubstate = substate;
		this.currentSubstate.onStart();
	}

	@Override
	public void update() {
		DungeonPokemonRenderer.instance.update();
		this.logger.update();
		this.currentSubstate.update();
	}

}

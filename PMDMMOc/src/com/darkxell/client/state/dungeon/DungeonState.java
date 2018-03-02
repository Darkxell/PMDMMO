package com.darkxell.client.state.dungeon;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.function.Predicate;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.renderers.floor.DungeonItemsRenderer;
import com.darkxell.client.renderers.floor.DungeonPokemonRenderer;
import com.darkxell.client.renderers.floor.FloorRenderer;
import com.darkxell.client.renderers.floor.FloorVisibility;
import com.darkxell.client.renderers.floor.GridRenderer;
import com.darkxell.client.renderers.floor.PokemonRenderer;
import com.darkxell.client.renderers.floor.ShadowRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;
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
			return this.parent.isMain() && this.parent.currentSubstate == this;
		}

	}

	public final ActionSelectionState actionSelectionState;
	private Point camera = new Point(0, 0);
	/** The Pokémon around which to draw. */
	private DungeonPokemon cameraPokemon;
	/** The current substate. */
	private DungeonSubState currentSubstate;
	boolean diagonal = false, rotating = false;
	public final FloorRenderer floorRenderer;
	public final FloorVisibility floorVisibility;
	public final GridRenderer gridRenderer;
	public final DungeonItemsRenderer itemRenderer;
	public final DungeonLogger logger;
	public final DungeonPokemonRenderer pokemonRenderer;
	/** The last Camera. */
	private Point previousCamera;
	public final ShadowRenderer shadowRenderer;

	public DungeonState()
	{
		this.cameraPokemon = Persistance.player.getDungeonLeader();
		Persistance.dungeonRenderer = new MasterDungeonRenderer();
		this.floorRenderer = new FloorRenderer();
		this.gridRenderer = new GridRenderer();
		this.itemRenderer = new DungeonItemsRenderer();
		this.pokemonRenderer = new DungeonPokemonRenderer();
		this.shadowRenderer = new ShadowRenderer();
		Persistance.dungeonRenderer.addRenderer(this.floorRenderer);
		Persistance.dungeonRenderer.addRenderer(this.gridRenderer);
		Persistance.dungeonRenderer.addRenderer(this.itemRenderer);
		Persistance.dungeonRenderer.addRenderer(this.shadowRenderer);
		this.placeTeam();

		this.logger = new DungeonLogger(this);
		this.currentSubstate = this.actionSelectionState = new ActionSelectionState(this);
		this.currentSubstate.onStart();
		this.floorVisibility = new FloorVisibility();
	}

	public Point camera()
	{
		return this.camera;
	}

	public DungeonPokemon getCameraPokemon()
	{
		return this.cameraPokemon;
	}

	@Override
	public void onEnd()
	{
		super.onEnd();
		this.logger.hideMessages();
		this.diagonal = false;
		this.rotating = false;
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
	public void onMouseClick(int x, int y)
	{
		super.onMouseClick(x, y);
		this.currentSubstate.onMouseClick(x, y);
	}

	@Override
	public void onMouseMove(int x, int y)
	{
		super.onMouseMove(x, y);
		this.currentSubstate.onMouseMove(x, y);
	}

	@Override
	public void onMouseRightClick(int x, int y)
	{
		super.onMouseRightClick(x, y);
		this.currentSubstate.onMouseRightClick(x, y);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		this.floorVisibility.onCameraMoved();
	}

	private void placeTeam()
	{
		Point spawn = Persistance.floor.teamSpawn;
		Persistance.floor.tileAt(spawn.x, spawn.y).setPokemon(Persistance.player.getDungeonLeader());
		Persistance.dungeon.insertActor(Persistance.player.getDungeonLeader(), 0);
		this.pokemonRenderer.register(Persistance.player.getDungeonLeader()).sprite.setShadowColor(PokemonSprite.ALLY_SHADOW);

		ArrayList<Tile> candidates = new ArrayList<Tile>();
		Tile initial = Persistance.player.getDungeonLeader().tile();
		candidates.add(initial.adjacentTile(Direction.WEST));
		candidates.add(initial.adjacentTile(Direction.EAST));
		candidates.add(initial.adjacentTile(Direction.SOUTH));
		candidates.add(initial.adjacentTile(Direction.NORTH));
		candidates.add(initial.adjacentTile(Direction.NORTHWEST));
		candidates.add(initial.adjacentTile(Direction.NORTHEAST));
		candidates.add(initial.adjacentTile(Direction.SOUTHWEST));
		candidates.add(initial.adjacentTile(Direction.SOUTHEAST));
		candidates.removeIf(new Predicate<Tile>() {
			@Override
			public boolean test(Tile t)
			{
				return t.getPokemon() != null || t.type() == TileType.WALL || t.type() == TileType.WATER || t.type() == TileType.LAVA
						|| t.type() == TileType.AIR;
			}
		});

		DungeonPokemon[] team = Persistance.player.getDungeonTeam();

		for (int i = team.length - 1; i > 0; --i)
		{
			if (team[i].isFainted()) continue;
			if (candidates.size() == 0)
			{
				Logger.e("DungeonState.placeAllies() @124 : Could not find a spawn location for ally " + team[i].getNickname() + "!");
				continue;
			}
			Persistance.floor.tileAt(candidates.get(0).x, candidates.get(0).y).setPokemon(team[i]);
			Persistance.floor.aiManager.register(team[i]);
			Persistance.dungeon.insertActor(team[i], 1);
			candidates.remove(0);
			this.pokemonRenderer.register(team[i]).sprite.setShadowColor(PokemonSprite.ALLY_SHADOW);
		}

		for (int i = team.length - 1; i >= 0; --i)
			Persistance.eventProcessor.addToPending(team[i].onFloorStart(Persistance.floor));
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		PokemonRenderer r = Persistance.dungeonState.pokemonRenderer.getRenderer(this.cameraPokemon);
		int x = (int) (r.x() + TILE_SIZE / 2 - width / 2), y = (int) (r.y() + TILE_SIZE / 2 - height / 2);
		this.camera = new Point(x, y);

		if (this.previousCamera == null || !this.previousCamera.equals(this.camera()))
		{
			this.floorRenderer.setXY(x, y);
			this.gridRenderer.setXY(x, y);
			this.itemRenderer.setXY(x, y);
			this.pokemonRenderer.setXY(x, y);
			this.shadowRenderer.setXY(x, y);
		}

		g.translate(-x, -y);
		Persistance.dungeonRenderer.render(g, width, height);
		g.translate(x, y);
		this.currentSubstate.render(g, width, height);

		Color weather = Persistance.floor.currentWeather().weather.layer;
		if (weather != null)
		{
			g.setColor(weather);
			g.fillRect(0, 0, width, height);
		}

		this.logger.render(g, width, height);

		this.previousCamera = (Point) this.camera().clone();
	}

	public void setCamera(DungeonPokemon pokemon)
	{
		this.cameraPokemon = pokemon;
		this.floorVisibility.onCameraMoved();
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
		Persistance.dungeonRenderer.update();
		this.pokemonRenderer.update();
		if (this.isMain()) this.logger.update();
		this.currentSubstate.update();
	}

}

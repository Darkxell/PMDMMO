package com.darkxell.client.state.dungeon;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.function.Predicate;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.renderers.floor.*;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Directions;
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
	/** The Pok�mon around which to draw. */
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
		Persistance.soundmanager.setBackgroundMusic(SoundsHolder.getSong("07 Tiny Woods.mp3"));
		Persistance.eventProcessor.processPending();
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
				Logger.e("DungeonState.placeAllies() @124 : Could not find a spawn location for ally " + team[i].pokemon.getNickname() + "!");
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
		this.logger.update();
		this.currentSubstate.update();
	}

}

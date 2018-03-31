package com.darkxell.client.state.map;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.floor.PokemonRenderer;
import com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset;
import com.darkxell.client.resources.images.tilesets.DungeonMapTileset;
import com.darkxell.client.state.dungeon.NextFloorState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;

public class DungeonFloorMap extends AbstractDisplayMap
{

	public static final int PLAYER_TICK = 30;
	public static final int TILE_SIZE = 4;
	public static final Color walls = Color.WHITE;

	/** True if the default location needs has been set. Reset to false for each new floor. */
	private boolean defaultLocationSet = false;
	private Floor floor;
	/** True if the map should follow the leader. Set to false whenever the player moves the map themselves. */
	private boolean followLeader = true;
	private int tick = 0;
	public final DungeonMapTileset tileset = DungeonMapTileset.INSTANCE;
	/** Map offsets. */
	private int x = 0, y = 0;

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		if (Persistance.dungeonState == null) return;

		if ((this.followLeader || !this.defaultLocationSet) && this.floor != null && Persistance.player.getDungeonLeader().tile() != null)
		{
			PokemonRenderer renderer = Persistance.dungeonState.pokemonRenderer.getRenderer(Persistance.player.getDungeonLeader());
			this.x = (int) (renderer.x() * TILE_SIZE / AbstractDungeonTileset.TILE_SIZE - width / 2);
			this.y = (int) (renderer.y() * TILE_SIZE / AbstractDungeonTileset.TILE_SIZE - height / 2);
			this.defaultLocationSet = true;
		}

		g.translate(-this.x, -this.y);

		if (this.floor != null)
		{
			int xStart = this.x / TILE_SIZE, yStart = this.y / TILE_SIZE;

			for (int x = xStart; x < this.floor.getWidth() && x <= xStart + width / TILE_SIZE; ++x)
				for (int y = yStart; y < this.floor.getHeight() && y <= yStart + height / TILE_SIZE; ++y)
				{
					if (x < 0 || y < 0) continue;
					Tile tile = this.floor.tileAt(x, y);
					if (tile == null) Logger.e("null tile at " + x + ", " + y);
					else
					{
						int tx = tile.x * TILE_SIZE, ty = tile.y * TILE_SIZE;
						if (Persistance.dungeonState.floorVisibility.isVisible(tile))
						{
							boolean isMain = tile.getPokemon() == Persistance.player.getDungeonLeader();
							if ((this.tick >= PLAYER_TICK || !isMain) && tile.getPokemon() != null) g.drawImage(this.tileset.ground(), tx, ty, null);
							else if (tile.getItem() != null && Persistance.dungeonState.floorVisibility.hasVisibleItem(tile))
								g.drawImage(this.tileset.item(), tx, ty, null);
							else if (tile.trap == TrapRegistry.WONDER_TILE) g.drawImage(this.tileset.wonder(), tx, ty, null);
							else if (tile.trapRevealed) g.drawImage(this.tileset.trap(), tx, ty, null);
							else if (tile.type() == TileType.STAIR) g.drawImage(this.tileset.stairs(), tx, ty, null);
							else if (tile.type() == TileType.WARP_ZONE) g.drawImage(this.tileset.warpzone(), tx, ty, null);
							else if (tile.type() == TileType.GROUND) g.drawImage(this.tileset.ground(), tx, ty, null);
							else
							{
								g.setColor(walls);
								if (tile.isAdjacentWalkable(Direction.NORTH)) g.drawLine(tx, ty, tx + 3, ty);
								if (tile.isAdjacentWalkable(Direction.EAST)) g.drawLine(tx + 3, ty, tx + 3, ty + 3);
								if (tile.isAdjacentWalkable(Direction.SOUTH)) g.drawLine(tx, ty + 3, tx + 3, ty + 3);
								if (tile.isAdjacentWalkable(Direction.WEST)) g.drawLine(tx, ty, tx, ty + 3);
								if (tile.isAdjacentWalkable(Direction.NORTHEAST)) g.drawLine(tx + 3, ty, tx + 3, ty);
								if (tile.isAdjacentWalkable(Direction.SOUTHEAST)) g.drawLine(tx + 3, ty + 3, tx + 3, ty + 3);
								if (tile.isAdjacentWalkable(Direction.SOUTHWEST)) g.drawLine(tx, ty + 3, tx, ty + 3);
								if (tile.isAdjacentWalkable(Direction.NORTHWEST)) g.drawLine(tx, ty, tx, ty);
							}
						} else if (tile.getItem() != null && Persistance.dungeonState.floorVisibility.hasVisibleItem(tile))
							g.drawImage(this.tileset.item(), tx, ty, null);
					}
				}

			for (PokemonRenderer renderer : Persistance.dungeonState.pokemonRenderer.listRenderers())
				if (Persistance.dungeonState.floorVisibility.isVisible(renderer.pokemon))
				{
					boolean isMain = renderer.pokemon == Persistance.player.getDungeonLeader();
					int x = (int) (renderer.x() * TILE_SIZE / AbstractDungeonTileset.TILE_SIZE),
							y = (int) (renderer.y() * TILE_SIZE / AbstractDungeonTileset.TILE_SIZE);
					if (isMain && this.tick >= PLAYER_TICK) g.drawImage(this.tileset.player(), x, y, null);
					else if (!isMain && Persistance.dungeonState.floorVisibility.isMapVisible(renderer.pokemon))
					{
						if (Persistance.player.isAlly(renderer.pokemon)) g.drawImage(this.tileset.ally(), x, y, null);
						else g.drawImage(this.tileset.enemy(), x, y, null);
					}
				}
		}

		g.translate(this.x, this.y);

		if (Persistance.stateManager.getCurrentState() instanceof NextFloorState)
		{
			int alpha = ((NextFloorState) Persistance.stateManager.getCurrentState()).fading();
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, width, height);
		}
	}

	@Override
	public void update()
	{
		if (Persistance.floor != this.floor)
		{
			this.defaultLocationSet = false;
			this.floor = Persistance.floor;
		}

		final int mapSpeed = 3;

		if (Keys.isPressed(Keys.KEY_MAP_UP)) this.y -= mapSpeed;
		if (Keys.isPressed(Keys.KEY_MAP_DOWN)) this.y += mapSpeed;
		if (Keys.isPressed(Keys.KEY_MAP_LEFT)) this.x -= mapSpeed;
		if (Keys.isPressed(Keys.KEY_MAP_RIGHT)) this.x += mapSpeed;
		if (Keys.isPressed(Keys.KEY_MAP_RESET))
		{
			this.followLeader = true;
			this.defaultLocationSet = false;
		}
		if (Keys.isPressed(Keys.KEY_MAP_UP) || Keys.isPressed(Keys.KEY_MAP_DOWN) || Keys.isPressed(Keys.KEY_MAP_LEFT) || Keys.isPressed(Keys.KEY_MAP_RIGHT))
			this.followLeader = false;

		++this.tick;
		if (this.tick >= PLAYER_TICK * 2) this.tick = 0;
	}

}

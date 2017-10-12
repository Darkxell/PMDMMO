package com.darkxell.client.state.map;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.resources.images.tilesets.DungeonMapTileset;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Directions;
import com.darkxell.common.util.Logger;

public class DungeonFloorMap extends AbstractDisplayMap
{

	public static final int TILE_SIZE = 4;
	public static final Color walls = Color.WHITE;

	/** True if the default location needs has been set. Reset to false for each new floor. */
	private boolean defaultLocationSet = false;
	private Floor floor;
	/** True if the map should follow the leader. Set to false whenever the player moves the map themselves. */
	private boolean followLeader = true;
	public final DungeonMapTileset tileset = DungeonMapTileset.INSTANCE;
	/** Map offsets. */
	private int x = 0, y = 0;

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if ((this.followLeader || !this.defaultLocationSet) && this.floor != null && Persistance.player.getDungeonPokemon().tile != null)
		{
			this.x = Persistance.player.getDungeonPokemon().tile.x * TILE_SIZE - width / 2;
			this.y = Persistance.player.getDungeonPokemon().tile.y * TILE_SIZE - height / 2;
			this.defaultLocationSet = true;
		}

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		g.translate(-this.x, -this.y);

		if (this.floor != null)
		{
			int xStart = this.x - width / 2, yStart = this.y - height / 2;
			if (xStart < 0) xStart = 0;
			if (yStart < 0) yStart = 0;

			for (int x = xStart; x < this.floor.getWidth() && x <= xStart + width / TILE_SIZE + 1; ++x)
				for (int y = yStart; y < this.floor.getHeight() && y <= yStart + height / TILE_SIZE + 1; ++y)
				{
					Tile tile = this.floor.tileAt(x, y);
					if (tile == null) Logger.e("null tile at " + x + ", " + y);
					else
					{
						int tx = tile.x * TILE_SIZE, ty = tile.y * TILE_SIZE;
						if (tile.getPokemon() != null)
						{
							if (tile.getPokemon() == Persistance.player.getDungeonPokemon()) g.drawImage(this.tileset.player(), tx, ty, null);
							else if (Persistance.player.isAlly(tile.getPokemon().pokemon)) g.drawImage(this.tileset.ally(), tx, ty, null);
							else g.drawImage(this.tileset.enemy(), tx, ty, null);
						} else if (tile.getItem() != null) g.drawImage(this.tileset.item(), tx, ty, null);
						else if (tile.trap == TrapRegistry.WONDER_TILE) g.drawImage(this.tileset.wonder(), tx, ty, null);
						else if (tile.trapRevealed) g.drawImage(this.tileset.trap(), tx, ty, null);
						else if (tile.type() == TileType.STAIR) g.drawImage(this.tileset.stairs(), tx, ty, null);
						else if (tile.type() == TileType.WARP_ZONE) g.drawImage(this.tileset.warpzone(), tx, ty, null);
						else if (tile.type() == TileType.GROUND) g.drawImage(this.tileset.ground(), tx, ty, null);
						else
						{
							g.setColor(walls);
							if (tile.isAdjacentWalkable(Directions.NORTH)) g.drawLine(tx, ty, tx + 3, ty);
							if (tile.isAdjacentWalkable(Directions.EAST)) g.drawLine(tx + 3, ty, tx + 3, ty + 3);
							if (tile.isAdjacentWalkable(Directions.SOUTH)) g.drawLine(tx, ty + 3, tx + 3, ty + 3);
							if (tile.isAdjacentWalkable(Directions.WEST)) g.drawLine(tx, ty, tx, ty + 3);
							if (tile.isAdjacentWalkable(Directions.NORTHEAST)) g.drawLine(tx + 3, ty, tx + 3, ty);
							if (tile.isAdjacentWalkable(Directions.SOUTHEAST)) g.drawLine(tx + 3, ty + 3, tx + 3, ty + 3);
							if (tile.isAdjacentWalkable(Directions.SOUTHWEST)) g.drawLine(tx, ty + 3, tx, ty + 3);
							if (tile.isAdjacentWalkable(Directions.NORTHWEST)) g.drawLine(tx, ty, tx, ty);
						}
					}
				}
		}

		g.translate(this.x, this.y);
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
		if (Keys.isPressed(Keys.KEY_MAP_UP) || Keys.isPressed(Keys.KEY_MAP_DOWN) || Keys.isPressed(Keys.KEY_MAP_LEFT) || Keys.isPressed(Keys.KEY_MAP_RIGHT)) this.followLeader = false;
	}

}

package com.darkxell.common.dungeon.floor.layout;

import java.awt.Point;

import com.darkxell.common.dungeon.floor.Room;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;

/** A Layout with random rooms in a grid-like pattern. */
public class GridRoomsLayout extends Layout {

	private static final int OFFSET = 5;
	private int gridwidth;
	private int gridheight;
	/** Maximum dimensions of rooms. Width and Height can be switched. */
	public final int maxRoomWidth, maxRoomHeight;
	/** Minimum dimensions of rooms. Width and Height can be switched. */
	public final int minRoomWidth, minRoomHeight;
	private Point[][] roomcenters;

	public GridRoomsLayout(int id, int gridwidth, int gridheight, int minRoomWidth, int minRoomHeight, int maxRoomWidth,
			int maxRoomHeight) {
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
		this.minRoomWidth = minRoomWidth;
		this.minRoomHeight = minRoomHeight;
		this.maxRoomWidth = maxRoomWidth;
		this.maxRoomHeight = maxRoomHeight;
		this.roomcenters = new Point[gridwidth][gridheight];

	}

	@Override
	protected void generateLiquids() {

	}

	@Override
	protected void generateRooms() {
		int gridCellWidth = this.maxRoomWidth + OFFSET;
		int gridCellHeight = this.maxRoomHeight + OFFSET;
		this.floor.tiles = new Tile[gridCellWidth * this.gridwidth][gridCellHeight * this.gridheight];
		for (int x = 0; x < this.floor.tiles.length; x++)
			for (int y = 0; y < this.floor.tiles[0].length; y++)
				this.floor.tiles[x][y] = new Tile(this.floor, x, y, TileType.WALL);
		// Sets the centers.
		for (int x = 0; x < this.gridwidth; x++)
			for (int y = 0; y < this.gridheight; y++)
				this.roomcenters[x][y] = new Point((gridCellWidth / 2) + (gridCellWidth * x),
						(gridCellHeight / 2) + (gridCellHeight * y));
		// Create new rooms
		this.floor.rooms = new Room[this.gridheight * this.gridwidth];
		for (int x = 0; x < this.gridwidth; x++)
			for (int y = 0; y < this.gridheight; y++) {
				int roomX = this.roomcenters[x][y].x
						- ((this.random.nextInt(maxRoomWidth - minRoomWidth) + minRoomWidth) / 2);
				int roomY = this.roomcenters[x][y].y
						- ((this.random.nextInt(maxRoomHeight - minRoomHeight) + minRoomHeight) / 2);
				int roomWidth = this.roomcenters[x][y].x
						+ ((this.random.nextInt(maxRoomWidth - minRoomWidth) + minRoomWidth) / 2) - roomX;
				int roomHeight = this.roomcenters[x][y].y
						+ ((this.random.nextInt(maxRoomHeight - minRoomHeight) + minRoomHeight) / 2) - roomY;
				this.floor.rooms[x + (y * this.gridwidth)] = new Room(this.floor, roomX, roomY, roomWidth, roomHeight,
						false);
			}

		for (int i = 0; i < this.floor.rooms.length; ++i)
			for (int x = this.floor.rooms[i].x; x < this.floor.rooms[i].width + this.floor.rooms[i].x; ++x)
				for (int y = this.floor.rooms[i].y; y < this.floor.rooms[i].height + this.floor.rooms[i].y; ++y)
					this.floor.tiles[x][y].setType(TileType.GROUND);
		// TODO : remove rooms randomely
	}

	@Override
	protected void generatePaths() {
		for (int x = 0; x < roomcenters.length; ++x)
			for (int y = 0; y < roomcenters[0].length; ++y) {
				Room r1 = this.floor.roomAt(roomcenters[x][y].x, roomcenters[x][y].y);
				if (x != roomcenters.length - 1) {
					int startx1 = r1.x + r1.width;
					int starty1 = this.random.nextInt(r1.height) + r1.y;
					Room r2 = this.floor.roomAt(roomcenters[x + 1][y].x, roomcenters[x + 1][y].y);
					int endx1 = r2.x;
					int endy1 = this.random.nextInt(r2.height) + r2.y;
					for (int i = startx1; i < endx1; ++i)
						this.floor.tiles[i][i - startx1 > (endx1 - startx1) / 2 ? endy1 : starty1]
								.setType(TileType.GROUND);
					if (starty1 > endy1)
						for (int i = endy1; i <= starty1; ++i)
							this.floor.tiles[startx1 + (endx1 - startx1) / 2][i].setType(TileType.GROUND);
					else if (starty1 < endy1)
						for (int i = starty1; i <= endy1; ++i)
							this.floor.tiles[startx1 + (endx1 - startx1) / 2][i].setType(TileType.GROUND);
				}
				if (y != roomcenters[0].length - 1) {
					int startx1 = this.random.nextInt(r1.width) + r1.x;
					int starty1 = r1.y + r1.height;
					Room r2 = this.floor.roomAt(roomcenters[x][y + 1].x, roomcenters[x][y + 1].y);
					int endx1 = this.random.nextInt(r2.width) + r2.x;
					int endy1 = r2.y;
					for (int i = starty1; i < endy1; ++i)
						this.floor.tiles[i - starty1 > (endy1 - starty1) / 2 ? endx1 : startx1][i]
								.setType(TileType.GROUND);
					if (startx1 > endx1)
						for (int i = endx1; i <= startx1; ++i)
							this.floor.tiles[i][starty1 + (endy1 - starty1) / 2].setType(TileType.GROUND);
					else if (startx1 < endx1)
						for (int i = startx1; i <= endx1; ++i)
							this.floor.tiles[i][starty1 + (endy1 - starty1) / 2].setType(TileType.GROUND);
				}
			}
	}

	@Override
	protected void placeItems() {
	}

	@Override
	protected void placeTeam() {
		Tile t = this.floor.randomRoom(this.random).randomTile(this.random);
		this.floor.teamSpawn = new Point(t.x, t.y);
	}

	@Override
	protected void summonPokemon() {
	}

}

package com.darkxell.common.dungeon.floor.layout;

import java.awt.Point;

import com.darkxell.common.dungeon.floor.Tile;

/** A Layout with random rooms in a grid-like pattern. */
public class GridRoomsLayout extends Layout {

	private int gridwidth;
	private int gridheight;
	/** Maximum dimensions of rooms. Width and Height can be switched. */
	public final int maxRoomWidth, maxRoomHeight;
	/** Minimum dimensions of rooms. Width and Height can be switched. */
	public final int minRoomWidth, minRoomHeight;

	public GridRoomsLayout(int id, int gridwidth, int gridheight, int minRoomWidth, int minRoomHeight, int maxRoomWidth,
			int maxRoomHeight) {
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
		this.minRoomWidth = minRoomWidth;
		this.minRoomHeight = minRoomHeight;
		this.maxRoomWidth = maxRoomWidth;
		this.maxRoomHeight = maxRoomHeight;
	}

	@Override
	protected void generateLiquids() {

	}

	@Override
	protected void generateRooms() {

	}

	@Override
	protected void generatePaths() {
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
	protected void placeTraps() {
	}

	@Override
	protected void summonPokemon() {
	}

}

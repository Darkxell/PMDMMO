package com.darkxell.common.dungeon.floor.layout;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Room;

/** A Layout with random rooms in a grid-like pattern. */
public class GridRoomsLayout extends Layout
{

	public static final int MIN_SPACE = 4, MAX_SPACE = 8;

	/** Temporary variables to store grid dimensions. */
	private Dimension dimensions;
	/** Temporary variable to store the space between slots. */
	private int horizSpace, vertiSpace;
	/** Maximum dimensions of rooms. Width and Height can be switched. */
	public final int maxRoomWidth, maxRoomHeight;
	/** Maximum number of rooms. Width and Height can be switched. */
	public final int maxWidth, maxHeight;
	/** Minimum dimensions of rooms. Width and Height can be switched. */
	public final int minRoomWidth, minRoomHeight;
	/** Minimum number of rooms. Width and Height can be switched. */
	public final int minWidth, minHeight;
	/** Temporary variable to store the Room dimensions. */
	private Dimension[][] sizes;

	public GridRoomsLayout(int id, int minWidth, int minHeight, int maxWidth, int maxHeight, int minRoomWidth, int minRoomHeight, int maxRoomWidth,
			int maxRoomHeight)
	{
		super(id, minWidth * minHeight, maxWidth * maxHeight);
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.minRoomWidth = minRoomWidth;
		this.minRoomHeight = minRoomHeight;
		this.maxRoomWidth = maxRoomWidth;
		this.maxRoomHeight = maxRoomHeight;
	}

	private void createGrid()
	{
		// TODO better grid creation
		int count = this.rooms.length;
		int w, h;
		if (count <= 4) w = h = 2;
		else if (this.random.nextInt(2) >= 1)
		{
			w = 3;
			h = 2;
		} else
		{
			w = 2;
			h = 3;
		}

		this.sizes = new Dimension[w][h];
	}

	@Override
	protected void generateLiquids()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void generateRooms()
	{
		this.createGrid();

		this.dimensions = new Dimension(0, 0);
		for (int i = 0; i < this.rooms.length; ++i)
		{
			Point p = this.newGridPosition();
			this.sizes[p.x][p.y] = this.randomSize();
			this.dimensions = new Dimension(Math.max(this.dimensions.width, this.sizes[p.x][p.y].width), Math.max(this.dimensions.height,
					this.sizes[p.x][p.y].height));
		}
		this.dimensions = new Dimension(this.dimensions.width + 2, this.dimensions.height + 2);

		this.horizSpace = this.random.nextInt(MAX_SPACE - MIN_SPACE + 1) + MIN_SPACE;
		this.vertiSpace = this.random.nextInt(MAX_SPACE - MIN_SPACE + 1) + MIN_SPACE;
		int totalWidth = this.sizes.length * this.dimensions.width + (this.sizes.length - 1) * this.horizSpace;
		int totalHeight = this.sizes[0].length * this.dimensions.height + (this.sizes[0].length - 1) * this.vertiSpace;

		int startX = this.tiles.length / 2 - totalWidth / 2;
		int y = this.tiles[0].length / 2 - totalHeight / 2;
		int roomID = 0;

		for (int i = 0; i < this.sizes.length; ++i)
		{
			int x = startX;
			for (int j = 0; j < this.sizes[0].length; ++j)
			{
				if (this.sizes[i][j] != null)
				{
					int xPos = this.random.nextInt(this.dimensions.width - this.sizes[i][j].width + 1) + x;
					int yPos = this.random.nextInt(this.dimensions.height - this.sizes[i][j].height + 1) + y;
					this.rooms[roomID] = new Room(this.floor, xPos, yPos, this.sizes[i][j].width, this.sizes[i][j].height, false);
					++roomID;
				}
			}
		}

		this.sizes = null;
		this.dimensions = null;
	}

	/** @return A Random location for a Room. */
	private Point newGridPosition()
	{
		ArrayList<Point> candidates = new ArrayList<Point>();
		for (int x = 0; x < this.sizes.length; ++x)
			for (int y = 0; y < this.sizes[x].length; ++y)
				if (this.sizes[x][y] == null) candidates.add(new Point(x, y));
		return candidates.get(this.random.nextInt(candidates.size()));
	}

	/** @return A random size for a Room. */
	private Dimension randomSize()
	{
		int width = this.random.nextInt(this.maxWidth - this.minWidth + 1) + this.minWidth;
		int height = this.random.nextInt(this.maxHeight - this.minHeight + 1) + this.minHeight;
		if (this.random.nextInt(2) >= 1)
		{
			int temp = width;
			width = height;
			height = temp;
		}
		return new Dimension(width, height);
	}

}

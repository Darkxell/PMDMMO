package com.darkxell.common.dungeon.floor.layout;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.function.Predicate;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Room;

/** A Layout with random rooms in a grid-like pattern. */
public class GridRoomsLayout extends Layout
{

	public static final int MIN_SPACE = 3, MAX_SPACE = 8;

	/** Temporary variable to store the row heights. */
	private int[] heights;
	/** Maximum dimensions of rooms. Width and Height can be switched. */
	public final int maxRoomWidth, maxRoomHeight;
	/** Maximum number of rooms. Width and Height can be switched. */
	public final int maxWidth, maxHeight;
	/** Minimum dimensions of rooms. Width and Height can be switched. */
	public final int minRoomWidth, minRoomHeight;
	/** Minimum number of rooms. Width and Height can be switched. */
	public final int minWidth, minHeight;
	/** Temporary variable to store the Rooms. */
	private Rectangle[][] rects;
	/** Temporary variable to store the column widths. */
	private int[] widths;

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

		this.rects = new Rectangle[w][h];
	}

	@Override
	protected void generateLiquids()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void generateRooms()
	{
		System.out.println(this.rooms.length);
		this.createGrid();

		// Create random sizes
		for (int i = 0; i < this.rooms.length; ++i)
		{
			Point p = this.newGridPosition();
			this.rects[p.x][p.y] = this.randomRectangle();
		}

		this.widths = new int[this.rects.length];
		this.heights = new int[this.rects[0].length];
		for (int x = 0; x < this.rects.length; ++x)
			for (int y = 0; y < this.rects[x].length; ++y)
				if (this.rects[x][y] != null)
				{
					this.widths[x] = Math.max(this.widths[x], this.rects[x][y].width);
					this.heights[y] = Math.max(this.heights[y], this.rects[x][y].height);
				}

		// Align rooms topleft with minimal space
		for (int x = 0; x < this.rects.length; ++x)
			for (int y = 0; y < this.rects[x].length; ++y)
				if (this.rects[x][y] != null)
				{
					for (int col = 0; col < x; ++col)
						this.rects[x][y].x += this.widths[col] + MIN_SPACE;

					for (int row = 0; row < y; ++row)
						this.rects[x][y].y += this.heights[row] + MIN_SPACE;
					System.out.println(this.rects[x][y]);
				}

		// Erode rooms
		while (this.totalWidth() > Floor.MAX_WIDTH - 2)
		{
			boolean flag = this.random.nextInt(4) < 1;
			Rectangle r = this.randomXReduceable();
			if (r == null) flag = true;
			if (flag)
			{
				int startX = this.random.nextInt(this.rects.length - 1) + 1;
				for (int x = startX; x < this.rects.length; ++x)
					for (int y = 0; y < this.rects[x].length; ++y)
						if (this.rects[x][y] != null) --this.rects[x][y].x;
			} else --r.width;
		}
		while (this.totalHeight() > Floor.MAX_HEIGHT - 2)
		{
			boolean flag = this.random.nextInt(4) < 1;
			Rectangle r = this.randomYReduceable();
			if (r == null) flag = true;
			if (flag)
			{
				int startY = this.random.nextInt(this.rects[0].length - 1) + 1;
				for (int y = startY; y < this.rects[0].length; ++y)
					for (int x = 0; x < this.rects.length; ++x)
						if (this.rects[x][y] != null) --this.rects[x][y].y;
			} else --r.height;
		}

		// Add potential additional space

		// Center rooms
		int xStart = Floor.ALL_WIDTH / 2 - this.totalWidth() / 2;
		int yStart = Floor.ALL_HEIGHT / 2 - this.totalHeight() / 2;
		System.out.println(xStart + "," + yStart);

		for (int x = 0; x < this.rects.length; ++x)
			for (int y = 0; y < this.rects[x].length; ++y)
				if (this.rects[x][y] != null)
				{
					this.rects[x][y].x += xStart;
					this.rects[x][y].y += yStart;
				}

		/* this.horizSpace = this.random.nextInt(MAX_SPACE - MIN_SPACE + 1) + MIN_SPACE; this.vertiSpace = this.random.nextInt(MAX_SPACE - MIN_SPACE + 1) + MIN_SPACE; int totalWidth = this.sizes.length * this.dimensions.width + (this.sizes.length - 1) * this.horizSpace; int totalHeight =
		 * this.sizes[0].length * this.dimensions.height + (this.sizes[0].length - 1) * this.vertiSpace;
		 * 
		 * while (totalWidth >= Floor.MAX_WIDTH - 2) { Dimension d = this.randomXReduceable(); if (d == null) { --this.horizSpace; totalWidth -= this.sizes.length; } else { --d.width; --totalWidth; } } while (totalHeight >= Floor.MAX_HEIGHT - 2) { Dimension d = this.randomYReduceable(); if (d ==
		 * null) { --this.vertiSpace; totalHeight -= this.sizes[0].length; } else { --d.height; --totalHeight; } }
		 * 
		 * int x = Floor.MAX_WIDTH / 2 - totalWidth / 2 + Floor.UNBREAKABLE + 1; int startY = Floor.MAX_HEIGHT / 2 - totalHeight / 2 + Floor.UNBREAKABLE + 1; */
		int roomID = 0;

		for (int i = 0; i < this.rects.length; ++i)
		{
			for (int j = 0; j < this.rects[0].length; ++j)
			{
				Rectangle r = this.rects[i][j];
				if (r != null)
				{
					this.rooms[roomID] = new Room(this.floor, r.x, r.y, r.width, r.height, false);
					++roomID;
				}
			}
		}

		this.rects = null;
		this.widths = null;
		this.heights = null;
	}

	/** @return A Random location for a Room. */
	private Point newGridPosition()
	{
		ArrayList<Point> candidates = new ArrayList<Point>();
		for (int x = 0; x < this.rects.length; ++x)
			for (int y = 0; y < this.rects[x].length; ++y)
				if (this.rects[x][y] == null) candidates.add(new Point(x, y));
		return candidates.get(this.random.nextInt(candidates.size()));
	}

	/** @return A random Room. */
	private Rectangle randomRectangle()
	{
		int width = this.random.nextInt(this.maxRoomWidth - this.minRoomWidth + 1) + this.minRoomWidth;
		int height = this.random.nextInt(this.maxRoomHeight - this.minRoomHeight + 1) + this.minRoomHeight;
		if (this.random.nextInt(2) >= 1)
		{
			int temp = width;
			width = height;
			height = temp;
		}
		return new Rectangle(0, 0, width, height);
	}

	/** @return A Random room which width can be reduced. */
	private Rectangle randomXReduceable()
	{
		ArrayList<Rectangle> candidates = new ArrayList<Rectangle>();
		for (int x = 0; x < this.rects.length; ++x)
			for (int y = 0; y < this.rects[x].length; ++y)
				candidates.add(this.rects[x][y]);

		candidates.removeIf(new Predicate<Rectangle>()
		{
			@Override
			public boolean test(Rectangle d)
			{
				return d == null || d.width <= Math.min(minRoomWidth, minRoomHeight);
			}
		});
		if (candidates.size() == 0) return null;
		return candidates.get(this.random.nextInt(candidates.size()));
	}

	/** @return A Random room which height can be reduced. */
	private Rectangle randomYReduceable()
	{
		ArrayList<Rectangle> candidates = new ArrayList<Rectangle>();
		for (int x = 0; x < this.rects.length; ++x)
			for (int y = 0; y < this.rects[x].length; ++y)
				candidates.add(this.rects[x][y]);

		candidates.removeIf(new Predicate<Rectangle>()
		{
			@Override
			public boolean test(Rectangle d)
			{
				return d == null || d.height <= Math.min(minRoomWidth, minRoomHeight);
			}
		});
		if (candidates.size() == 0) return null;
		return candidates.get(this.random.nextInt(candidates.size()));
	}

	private int totalHeight()
	{
		int minY = Integer.MAX_VALUE, maxY = 0;
		for (int y = 0; y < this.rects[0].length && minY == Integer.MAX_VALUE; ++y)
			for (int x = 0; x < this.rects.length; ++x)
				if (this.rects[x][y] != null) minY = Math.min(minY, this.rects[x][y].y);

		for (int y = this.rects[0].length - 1; y >= 0 && maxY == 0; --y)
			for (int x = 0; x < this.rects.length; ++x)
				if (this.rects[x][y] != null) maxY = Math.max(maxY, this.rects[x][y].y + this.rects[x][y].height);

		return maxY - minY + 1;
	}

	private int totalWidth()
	{
		int minX = Integer.MAX_VALUE, maxX = 0;
		for (int x = 0; x < this.rects.length && minX == Integer.MAX_VALUE; ++x)
			for (int y = 0; y < this.rects[0].length; ++y)
				if (this.rects[0][y] != null) minX = Math.min(minX, this.rects[0][y].x);

		for (int x = this.rects.length - 1; x >= 0 && maxX == 0; --x)
			for (int y = 0; y < this.rects[0].length; ++y)
				if (this.rects[x][y] != null) maxX = Math.max(maxX, this.rects[x][y].x + this.rects[x][y].width);

		return maxX - minX + 1;
	}

}

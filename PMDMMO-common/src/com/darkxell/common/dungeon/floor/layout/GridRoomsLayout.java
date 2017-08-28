package com.darkxell.common.dungeon.floor.layout;

import java.awt.Dimension;
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

	/** Temporary variable to store the Rooms. */
	private Rectangle[][] grid;
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
	/** Temporary variable to store the column widths. */
	private int[] widths;

	public GridRoomsLayout(int id, int minWidth, int minHeight, int maxWidth, int maxHeight, int minRoomWidth, int minRoomHeight, int maxRoomWidth,
			int maxRoomHeight)
	{
		super(id);
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.minRoomWidth = minRoomWidth;
		this.minRoomHeight = minRoomHeight;
		this.maxRoomWidth = maxRoomWidth;
		this.maxRoomHeight = maxRoomHeight;
	}

	@Override
	protected void generateLiquids()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void generateRooms()
	{
		// Grid size
		int w = this.random.nextInt(this.maxWidth - this.minWidth + 1) + this.minWidth;
		int h = this.random.nextInt(this.maxHeight - this.minHeight + 1) + this.minHeight;
		while (Math.abs(w - h) > 1)
		{
			++w;
			--h;
		}

		if (this.random.nextInt(2) < 1)
		{
			int temp = w;
			w = h;
			h = temp;
		}

		int min = Math.min(h * (w - 1), w * (h - 1)) + 1;
		min = Math.max(this.minWidth * this.minHeight, min);
		int max = w * h;
		int count = this.random.nextInt(max - min + 1) + min;

		this.rooms = new Room[count];
		this.grid = new Rectangle[w][h];

		// Slot size
		this.widths = new int[w * 2];
		this.heights = new int[h * 2];
		for (int i = 0; i < this.widths.length || i < this.heights.length; ++i)
		{
			int wi, he;
			if (i % 2 == 0)
			{
				wi = this.maxRoomWidth;
				he = this.maxRoomHeight;
			} else wi = he = this.random.nextInt(MAX_SPACE - MIN_SPACE + 1) + MIN_SPACE;

			if (this.random.nextInt(2) < 1)
			{
				int temp = wi;
				wi = he;
				he = temp;
			}

			if (i < this.widths.length) this.widths[i] = wi;
			if (i < this.heights.length) this.heights[i] = he;
		}

		// Erode slots
		while (this.totalWidth() > Floor.MAX_WIDTH - 2)
		{
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			for (int i = 0; i < this.widths.length; ++i)
				candidates.add(i);
			candidates.removeIf(new Predicate<Integer>()
			{
				@Override
				public boolean test(Integer i)
				{
					if (i % 2 == 0) return widths[i] <= minRoomWidth;
					return widths[i] <= MIN_SPACE;
				}
			});
			--this.widths[candidates.get(this.random.nextInt(candidates.size()))];
		}
		while (this.totalHeight() > Floor.MAX_HEIGHT - 2)
		{
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			for (int i = 0; i < this.heights.length; ++i)
				candidates.add(i);
			candidates.removeIf(new Predicate<Integer>()
			{
				@Override
				public boolean test(Integer i)
				{
					if (i % 2 == 0) return heights[i] <= minRoomHeight;
					return heights[i] <= MIN_SPACE;
				}
			});
			--this.heights[candidates.get(this.random.nextInt(candidates.size()))];
		}

		// Make grid
		int xPos = 0;
		int yPos = 0;
		for (int x = 0; x < this.grid.length; ++x)
		{
			yPos = 0;
			for (int y = 0; y < this.grid[x].length; ++y)
			{
				Dimension d = new Dimension(this.random.nextInt(this.widths[x * 2] - this.minRoomWidth + 1) + this.minRoomWidth,
						this.random.nextInt(this.heights[y * 2] - this.minRoomHeight + 1) + this.minRoomHeight);
				Rectangle startCandidates = new Rectangle(xPos, yPos, this.widths[x * 2] - d.width, this.heights[y * 2] - d.height);
				Point p = new Point(this.random.nextInt((int) startCandidates.getMaxX() - startCandidates.x + 1) + startCandidates.x,
						this.random.nextInt((int) startCandidates.getMaxY() - startCandidates.y + 1) + startCandidates.y);
				this.grid[x][y] = new Rectangle(p.x, p.y, d.width, d.height);
				yPos += this.heights[y * 2] + this.heights[y * 2 + 1];
			}
			xPos += this.widths[x * 2] + this.widths[x * 2 + 1];
		}

		// Randomize grid location
		int xStart = this.random.nextInt(Floor.MAX_WIDTH - 2 - this.totalWidth() + 1) + Floor.UNBREAKABLE + 1;
		int yStart = this.random.nextInt(Floor.MAX_HEIGHT - 2 - this.totalHeight() + 1) + Floor.UNBREAKABLE + 1;

		for (int x = 0; x < this.grid.length; ++x)
			for (int y = 0; y < this.grid[x].length; ++y)
				if (this.grid[x][y] != null)
				{
					this.grid[x][y].x += xStart;
					this.grid[x][y].y += yStart;
				}

		// Delete extra slots
		for (int i = count; i < w * h; ++i)
		{
			Point p = this.newGridPosition();
			this.grid[p.x][p.y] = null;
		}

		// Export to Rooms
		int roomID = 0;
		for (int x = 0; x < this.grid.length; ++x)
			for (int y = 0; y < this.grid[x].length; ++y)
			{
				Rectangle r = this.grid[x][y];
				if (r != null)
				{
					this.rooms[roomID] = new Room(this.floor, r.x, r.y, r.width, r.height, false);
					++roomID;
				}
			}

		this.grid = null;
		this.widths = null;
		this.heights = null;
	}

	/** @return A Random location for a Room to delete. */
	private Point newGridPosition()
	{
		ArrayList<Point> candidates = new ArrayList<Point>();
		for (int x = 0; x < this.grid.length; ++x)
			for (int y = 0; y < this.grid[x].length; ++y)
				if (this.grid[x][y] != null) candidates.add(new Point(x, y));
		return candidates.get(this.random.nextInt(candidates.size()));
	}

	private int totalHeight()
	{
		int height = 0;
		for (int i = 0; i < this.heights.length - 1; ++i)
			height += this.heights[i];
		return height;
	}

	private int totalWidth()
	{
		int width = 0;
		for (int i = 0; i < this.widths.length - 1; ++i)
			width += this.widths[i];
		return width;
	}

}

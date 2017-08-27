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

	/** @return The possible coordinates for the topleft corner of the input room. */
	private Rectangle availableSpace(Rectangle r, Rectangle room)
	{
		boolean improved = true;
		Rectangle test = null;
		while (improved)
		{
			improved = false;

			// Move left
			test = (Rectangle) r.clone();
			--test.x;
			if (this.isValid(test))
			{
				improved = true;
				r = (Rectangle) test.clone();
			}

			// Move up
			test = (Rectangle) r.clone();
			--test.y;
			if (this.isValid(test))
			{
				improved = true;
				r = (Rectangle) test.clone();
			}

			// Increase width
			test = (Rectangle) r.clone();
			++test.width;
			if (this.isValid(test))
			{
				improved = true;
				r = (Rectangle) test.clone();
			}

			// Increase height
			test = (Rectangle) r.clone();
			++test.height;
			if (this.isValid(test))
			{
				improved = true;
				r = (Rectangle) test.clone();
			}

		}
		r.width -= room.width;
		r.height -= room.height;
		return r;
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
		System.out.println(count + " rooms, " + w + "x" + h);

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

		System.out.println("Grid:");
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
				System.out.println(this.grid[x][y]);
				yPos += this.heights[y * 2] + this.heights[y * 2 + 1];
			}
			xPos += this.widths[x * 2] + this.widths[x * 2 + 1];
		}
		System.out.println("Size: " + this.totalWidth() + "x" + this.totalHeight());

		// Center grid
		int xStart = Floor.ALL_WIDTH / 2 - this.totalWidth() / 2;
		int yStart = Floor.ALL_HEIGHT / 2 - this.totalHeight() / 2;

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

		System.out.println("After export:");
		// Export to Rooms
		int roomID = 0;
		for (int x = 0; x < this.grid.length; ++x)
			for (int y = 0; y < this.grid[x].length; ++y)
			{
				Rectangle r = this.grid[x][y];
				System.out.println(r);
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

	/** @param except - These rooms will be excluded in the test.
	 * @return true if the input test room doesn't collide with any other room. */
	private boolean isValid(Rectangle test, Rectangle... except)
	{
		if (Floor.WALKABLE.x >= test.x || Floor.WALKABLE.y >= test.y || Floor.WALKABLE.getMaxX() <= test.getMaxX()
				|| Floor.WALKABLE.getMaxY() <= test.getMaxY()) return false;

		ArrayList<Rectangle> r = this.rooms();
		for (Rectangle rectangle : except)
			r.remove(rectangle);
		for (Rectangle rectangle : r)
			if (rectangle.intersects(test)) return false;
		return true;
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
		ArrayList<Rectangle> candidates = this.rooms();

		candidates.removeIf(new Predicate<Rectangle>()
		{
			@Override
			public boolean test(Rectangle d)
			{
				return d.width <= Math.min(minRoomWidth, minRoomHeight);
			}
		});
		if (candidates.size() == 0) return null;
		return candidates.get(this.random.nextInt(candidates.size()));
	}

	/** @return A Random room which height can be reduced. */
	private Rectangle randomYReduceable()
	{
		ArrayList<Rectangle> candidates = this.rooms();

		candidates.removeIf(new Predicate<Rectangle>()
		{
			@Override
			public boolean test(Rectangle d)
			{
				return d.height <= Math.min(minRoomWidth, minRoomHeight);
			}
		});
		if (candidates.size() == 0) return null;
		return candidates.get(this.random.nextInt(candidates.size()));
	}

	private ArrayList<Rectangle> rooms()
	{
		ArrayList<Rectangle> rooms = new ArrayList<Rectangle>();
		for (int x = 0; x < this.grid.length; ++x)
			for (int y = 0; y < this.grid[x].length; ++y)
				rooms.add(this.grid[x][y]);
		rooms.removeIf(new Predicate<Rectangle>()
		{

			@Override
			public boolean test(Rectangle t)
			{
				return t == null;
			}
		});
		return rooms;
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

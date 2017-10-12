package com.darkxell.client.renderers;

import java.awt.Graphics2D;

public abstract class AbstractRenderer implements Comparable<AbstractRenderer>
{

	/** Rendering locations. X is horizontal position, Y is vertical position, Z is drawing order. <br />
	 * Objects with lowest Z will be drawn first. Then if Z is equal, lowest Y will be drawn, then lowest X. */
	private int x, y, z;

	@Override
	public int compareTo(AbstractRenderer o)
	{
		if (this.z != o.z) return Integer.compare(this.z, o.z);
		if (this.y != o.y) return Integer.compare(this.y, o.y);
		return Integer.compare(this.x, o.x);
	}

	/** Draws this Renderer's Object.
	 * 
	 * @param g - The Graphics context.
	 * @param width - Width of the screen.
	 * @param height - Height of the screen. */
	public abstract void render(Graphics2D g, int width, int height);

	public void setX(int x)
	{
		this.x = x;
		// update Master Renderer
	}

	public void setY(int y)
	{
		this.y = y;
		// update Master Renderer
	}

	public void setZ(int z)
	{
		this.z = z;
		// update Master Renderer
	}

	public int x()
	{
		return this.x;
	}

	public int y()
	{
		return this.y;
	}

	public int z()
	{
		return this.z;
	}

}

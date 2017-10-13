package com.darkxell.client.renderers;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;

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
		Persistance.dungeonState.dungeonRenderer.onObjectUpdated();
	}

	public void setXY(int x, int y)
	{
		this.setX(x);
		this.setY(y);
	}

	public void setXYZ(int x, int y, int z)
	{
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	public void setY(int y)
	{
		this.y = y;
		Persistance.dungeonState.dungeonRenderer.onObjectUpdated();
	}

	public void setZ(int z)
	{
		this.z = z;
		Persistance.dungeonState.dungeonRenderer.onObjectUpdated();
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

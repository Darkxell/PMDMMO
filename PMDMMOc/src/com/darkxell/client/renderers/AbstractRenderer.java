package com.darkxell.client.renderers;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;

public abstract class AbstractRenderer implements Comparable<AbstractRenderer>
{

	/** True if this Renderer should be used. */
	public boolean shouldRender = true;
	/** Rendering locations. X is horizontal position, Y is vertical position, Z is drawing order. <br />
	 * Objects with lowest Z will be drawn first. Then if Z is equal, lowest Y will be drawn, then lowest X. */
	private double x, y, z;

	@Override
	public int compareTo(AbstractRenderer o)
	{
		if (this.z != o.z) return Double.compare(this.z, o.z);
		if (this.y != o.y) return Double.compare(this.y, o.y);
		return Double.compare(this.x, o.x);
	}

	/** Draws this Renderer's Object.
	 * 
	 * @param g - The Graphics context.
	 * @param width - Width of the screen.
	 * @param height - Height of the screen. */
	public abstract void render(Graphics2D g, int width, int height);

	public void setX(double x)
	{
		this.x = x;
		Persistance.dungeonState.dungeonRenderer.onObjectUpdated();
	}

	public void setXY(double x, double y)
	{
		this.setX(x);
		this.setY(y);
	}

	public void setXYZ(double x, double y, double z)
	{
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	public void setY(double y)
	{
		this.y = y;
		Persistance.dungeonState.dungeonRenderer.onObjectUpdated();
	}

	public void setZ(double z)
	{
		this.z = z;
		Persistance.dungeonState.dungeonRenderer.onObjectUpdated();
	}

	public double x()
	{
		return this.x;
	}

	public double y()
	{
		return this.y;
	}

	public double z()
	{
		return this.z;
	}

}

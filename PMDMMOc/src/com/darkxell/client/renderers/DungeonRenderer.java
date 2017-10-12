package com.darkxell.client.renderers;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;

public class DungeonRenderer
{

	private final ArrayList<AbstractRenderer> renderers = new ArrayList<AbstractRenderer>();
	private boolean updateRequired;

	public void addRenderer(AbstractRenderer renderer)
	{
		this.renderers.add(renderer);
		this.updateRequired = true;
	}

	public void clear()
	{
		this.renderers.clear();
	}

	public void onObjectUpdated()
	{
		this.updateRequired = true;
	}

	public void removeRenderer(AbstractRenderer renderer)
	{
		this.renderers.remove(renderer);
	}

	public void render(Graphics2D g, int width, int height)
	{
		if (this.updateRequired)
		{
			this.renderers.sort(Comparator.naturalOrder());
			this.updateRequired = false;
		}

		for (AbstractRenderer renderer : this.renderers)
			renderer.render(g, width, height);
	}

}

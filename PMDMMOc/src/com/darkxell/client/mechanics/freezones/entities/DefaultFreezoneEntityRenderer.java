package com.darkxell.client.mechanics.freezones.entities;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.renderers.AbstractRenderer;

public class DefaultFreezoneEntityRenderer extends AbstractRenderer
{

	public final FreezoneEntity entity;

	public DefaultFreezoneEntityRenderer(FreezoneEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		this.entity.print(g);
	}

}

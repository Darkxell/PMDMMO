package com.darkxell.client.mechanics.freezones.entities.renderers;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.common.util.DoubleRectangle;

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
		if (Persistance.debugdisplaymode)
		{
			g.setColor(new Color(20, 20, 200, 160));
			DoubleRectangle dbrct = this.entity.getHitbox(this.entity.posX, this.entity.posY);
			g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));
		}
	}

	@Override
	public void update()
	{
		this.setXY(this.entity.posX * 8, this.entity.posY * 8);
		super.update();
	}

}

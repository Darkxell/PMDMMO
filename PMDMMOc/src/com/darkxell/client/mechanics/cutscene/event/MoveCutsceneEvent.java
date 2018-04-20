package com.darkxell.client.mechanics.cutscene.event;

import java.awt.Point;

import org.jdom2.Element;

import com.darkxell.client.mechanics.animation.TravelAnimation;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.common.util.XMLUtils;

public class MoveCutsceneEvent extends CameraCutsceneEvent
{
	public static final double UNSPECIFIED = Double.MAX_VALUE;

	private CutsceneEntity entity;
	public final int target;

	public MoveCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, cutscene);
		this.target = XMLUtils.getAttribute(xml, "target", -1);
	}

	@Override
	public void onFinish()
	{
		super.onFinish();
		if (this.entity != null)
		{
			this.entity.xPos = this.travel.destination.getX();
			this.entity.yPos = this.travel.destination.getY();
		}
	}

	@Override
	public void onStart()
	{
		this.entity = this.cutscene.getEntity(this.target);
		if (this.entity != null)
		{
			double startX = this.entity.xPos, startY = this.entity.yPos;
			double destX = this.xPos == UNSPECIFIED ? startX : this.xPos, destY = this.yPos == UNSPECIFIED ? startY : this.yPos;
			this.travel = new TravelAnimation(new Point.Double(startX, startY), new Point.Double(destX, destY));
			this.duration = (int) Math.floor(this.travel.distance.distance(new Point(0, 0)) / this.speed);
		}
	}

	@Override
	public void update()
	{
		if (!this.isOver())
		{
			++this.tick;
			this.travel.update(this.tick * 1d / this.duration);
		}
		if (this.entity != null)
		{
			this.entity.xPos = this.travel.current().getX();
			this.entity.yPos = this.travel.current().getY();
		}
	}

}

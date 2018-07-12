package com.darkxell.client.mechanics.cutscene.event;

import java.awt.Point;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.TravelAnimation;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.common.util.XMLUtils;

public class CameraCutsceneEvent extends CutsceneEvent
{
	public static final double UNSPECIFIED = Double.MAX_VALUE;

	public final double speed;
	protected int tick = 0, duration = 0;
	protected TravelAnimation travel;
	public final double xPos, yPos;

	public CameraCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, CutsceneEventType.camera, cutscene);
		this.xPos = XMLUtils.getAttribute(xml, "xpos", UNSPECIFIED);
		this.yPos = XMLUtils.getAttribute(xml, "ypos", UNSPECIFIED);
		this.speed = XMLUtils.getAttribute(xml, "speed", 1.);
	}

	public CameraCutsceneEvent(int id, double xpos, double ypos, double speed)
	{
		super(id, CutsceneEventType.camera);
		this.xPos = xpos;
		this.yPos = ypos;
		this.speed = speed;
	}

	@Override
	public boolean isOver()
	{
		return this.tick == this.duration;
	}

	@Override
	public void onFinish()
	{
		super.onFinish();
		if (Persistance.freezoneCamera != null)
		{
			Persistance.freezoneCamera.x = this.travel.destination.getX();
			Persistance.freezoneCamera.y = this.travel.destination.getY();
		}
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if (Persistance.freezoneCamera != null)
		{
			double startX = Persistance.freezoneCamera.x, startY = Persistance.freezoneCamera.y;
			double destX = this.xPos == UNSPECIFIED ? startX : this.xPos, destY = this.yPos == UNSPECIFIED ? startY : this.yPos;
			this.travel = new TravelAnimation(new Point.Double(startX, startY), new Point.Double(destX, destY));
			this.duration = (int) Math.floor(this.travel.distance.distance(new Point(0, 0)) / this.speed);
		} else this.tick = this.duration;
	}

	@Override
	public String toString()
	{
		return this.displayID() + "Camera moves to X=" + (this.xPos == UNSPECIFIED ? "[UNCHANGED]" : this.xPos) + ", Y="
				+ (this.yPos == UNSPECIFIED ? "[UNCHANGED]" : this.yPos);
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		XMLUtils.setAttribute(root, "xpos", this.xPos, UNSPECIFIED);
		XMLUtils.setAttribute(root, "ypos", this.yPos, UNSPECIFIED);
		XMLUtils.setAttribute(root, "speed", this.speed, 1);
		return root;
	}

	@Override
	public void update()
	{
		super.update();
		if (!this.isOver())
		{
			++this.tick;
			this.travel.update(this.tick * 1d / this.duration);
		}
		if (Persistance.freezoneCamera != null)
		{
			Persistance.freezoneCamera.x = this.travel.current().getX();
			Persistance.freezoneCamera.y = this.travel.current().getY();
		}
	}

}

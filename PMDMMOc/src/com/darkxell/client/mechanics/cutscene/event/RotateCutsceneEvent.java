package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.common.util.XMLUtils;

public class RotateCutsceneEvent extends CutsceneEvent
{

	public static final int DEFAULT_SPEED = 10;

	private int currentDistance;
	public final int distance;
	private CutscenePokemon pokemon;
	public final int speed;
	public final int target;
	private int tick;

	public RotateCutsceneEvent(Element xml, CutsceneContext context)
	{
		super(xml, CutsceneEventType.rotate, context);
		this.target = XMLUtils.getAttribute(xml, "target", -1);
		this.distance = XMLUtils.getAttribute(xml, "distance", 0);
		this.speed = XMLUtils.getAttribute(xml, "speed", DEFAULT_SPEED);
	}

	public RotateCutsceneEvent(int id, CutsceneEntity entity, int distance, int speed)
	{
		super(id, CutsceneEventType.rotate);
		this.target = entity == null ? -1 : entity.id;
		this.distance = distance;
		this.speed = speed;
	}

	@Override
	public boolean isOver()
	{
		return this.currentDistance == Math.abs(this.distance);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		CutsceneEntity entity = this.context.parent().player.getEntity(this.target);
		if (entity != null && entity instanceof CutscenePokemon)
		{
			this.pokemon = (CutscenePokemon) entity;
			this.currentDistance = this.tick = 0;

			if (this.rotatesInstantly())
			{
				this.currentDistance = this.distance;
				for (int i = 0; i < Math.abs(this.distance); ++i)
					if (this.distance > 0) this.pokemon.facing = this.pokemon.facing.rotateClockwise();
					else this.pokemon.facing = this.pokemon.facing.rotateCounterClockwise();
			}
		} else this.currentDistance = this.distance;
	}

	private boolean rotatesInstantly()
	{
		return this.speed == 0;
	}

	@Override
	public String toString()
	{
		return this.displayID() + "(" + this.target + ") rotates clockwise " + this.distance + " times";
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		root.setAttribute("target", String.valueOf(this.target));
		root.setAttribute("distance", String.valueOf(this.distance));
		XMLUtils.setAttribute(root, "speed", this.speed, DEFAULT_SPEED);
		return root;
	}

	@Override
	public void update()
	{
		super.update();
		if (!this.isOver())
		{
			++this.tick;
			if (this.tick >= this.speed)
			{
				if (this.distance > 0) this.pokemon.facing = this.pokemon.facing.rotateClockwise();
				else this.pokemon.facing = this.pokemon.facing.rotateCounterClockwise();
				++this.currentDistance;
				this.tick = 0;
			}
		}
	}

}

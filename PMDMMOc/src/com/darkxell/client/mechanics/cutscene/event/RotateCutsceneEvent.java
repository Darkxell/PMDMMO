package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.common.util.XMLUtils;

public class RotateCutsceneEvent extends CutsceneEvent
{

	private int currentDistance;
	public final int distance;
	public final boolean instantly;
	private CutscenePokemon pokemon;
	public final int target;

	public RotateCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, CutsceneEventType.rotate, cutscene);
		this.target = XMLUtils.getAttribute(xml, "target", -1);
		this.instantly = XMLUtils.getAttribute(xml, "instantly", false);
		this.distance = XMLUtils.getAttribute(xml, "distance", 0);
	}

	public RotateCutsceneEvent(int id, CutsceneEntity entity, int distance, boolean instantly)
	{
		super(id, CutsceneEventType.rotate);
		this.target = entity == null ? -1 : entity.id;
		this.distance = distance;
		this.instantly = instantly;
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
		CutsceneEntity entity = this.cutscene.player.getEntity(this.target);
		if (entity != null && entity instanceof CutscenePokemon)
		{
			this.pokemon = (CutscenePokemon) entity;
			this.currentDistance = 0;

			if (this.instantly)
			{
				this.currentDistance = this.distance;
				for (int i = 0; i < Math.abs(this.distance); ++i)
					if (this.distance > 0) this.pokemon.facing = this.pokemon.facing.rotateClockwise();
					else this.pokemon.facing = this.pokemon.facing.rotateCounterClockwise();
			}
		} else this.currentDistance = this.distance;
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
		XMLUtils.setAttribute(root, "instantly", this.instantly, false);
		return root;
	}

	@Override
	public void update()
	{
		super.update();
		if (!this.isOver())
		{
			if (this.distance > 0) this.pokemon.facing = this.pokemon.facing.rotateClockwise();
			else this.pokemon.facing = this.pokemon.facing.rotateCounterClockwise();
		}
		++this.currentDistance;
	}

}

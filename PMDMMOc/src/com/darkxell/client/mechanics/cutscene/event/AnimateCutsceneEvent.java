package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.common.util.XMLUtils;

public class AnimateCutsceneEvent extends CutsceneEvent
{

	private PokemonAnimation animation;
	public final int animationID;
	private boolean couldntLoad = false;
	public final int target;

	public AnimateCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, CutsceneEventType.animate, cutscene);
		this.target = XMLUtils.getAttribute(xml, "target", -1);
		this.animationID = XMLUtils.getAttribute(xml, "animation", 0);
	}

	public AnimateCutsceneEvent(int id, int animation, CutsceneEntity target)
	{
		super(id, CutsceneEventType.animate);
		this.animationID = animation;
		this.target = target == null ? -1 : target.id;
	}

	@Override
	public boolean isOver()
	{
		if (this.couldntLoad) return true;
		if (this.animation == null) return false;
		return this.animation.isDelayOver();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		CutsceneEntity entity = this.cutscene.player.getEntity(this.target);
		if (entity != null && entity instanceof CutscenePokemon)
		{
			this.animation = Animations.getCutsceneAnimation(this.animationID, (CutscenePokemon) entity, null);
			if (this.animation == null) this.couldntLoad = true;
			else this.animation.start();
		} else this.couldntLoad = true;
	}

	@Override
	public String toString()
	{
		return this.displayID() + "Play animation " + this.animationID + " on (" + this.target + ")";
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		root.setAttribute("animation", String.valueOf(this.animationID));
		XMLUtils.setAttribute(root, "target", this.target, -1);
		return root;
	}

}

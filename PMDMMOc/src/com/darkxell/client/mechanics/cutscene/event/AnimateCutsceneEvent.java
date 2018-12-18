package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.renderers.pokemon.CutscenePokemonRenderer;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Localization;

public class AnimateCutsceneEvent extends CutsceneEvent
{
	public static enum AnimateCutsceneEventMode
	{
		PLAY,
		START,
		STOP
	}

	private PokemonAnimation animation;
	public final int animationID;
	private boolean couldntLoad = false;
	public final AnimateCutsceneEventMode mode;
	public final int target;

	public AnimateCutsceneEvent(Element xml, CutsceneContext context)
	{
		super(xml, CutsceneEventType.animate, context);
		this.target = XMLUtils.getAttribute(xml, "target", -1);
		this.animationID = XMLUtils.getAttribute(xml, "animation", 0);
		this.mode = AnimateCutsceneEventMode.valueOf(XMLUtils.getAttribute(xml, "mode", AnimateCutsceneEventMode.PLAY.name()));
	}

	public AnimateCutsceneEvent(int id, int animation, AnimateCutsceneEventMode mode, CutsceneEntity target)
	{
		super(id, CutsceneEventType.animate);
		this.animationID = animation;
		this.target = target == null ? -1 : target.id;
		this.mode = mode;
	}

	@Override
	public boolean isOver()
	{
		if (this.couldntLoad || this.mode != AnimateCutsceneEventMode.PLAY) return true;
		if (this.animation == null) return false;
		return this.animation.isDelayOver();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		CutsceneEntity entity = this.context.parent().player.getEntity(this.target);
		if (entity != null && entity instanceof CutscenePokemon)
		{
			this.animation = Animations.getCutsceneAnimation(this.animationID, (CutscenePokemon) entity, null);
			if (this.animation == null) this.couldntLoad = true;
			else
			{
				CutscenePokemonRenderer r = (CutscenePokemonRenderer) Persistance.currentmap.cutsceneEntityRenderers.getRenderer(entity);
				if (r == null) this.couldntLoad = true;
				else
				{
					if (this.mode == AnimateCutsceneEventMode.STOP) r.removeAnimation(this.animation.data);
					else
					{
						if (this.mode == AnimateCutsceneEventMode.START)
						{
							this.animation.source = this.animation.data;
							this.animation.plays = -1;
						}
						this.animation.start();
					}
				}
			}
		} else this.couldntLoad = true;
	}

	@Override
	public String toString()
	{
		String animName = this.animationID + "";
		if (Localization.containsKey("animation.custom." + this.animationID)) animName += "-" + Localization.translate("animation.custom." + this.animationID);
		String mode = "Play";
		if (this.mode == AnimateCutsceneEventMode.START) mode = "Start";
		if (this.mode == AnimateCutsceneEventMode.STOP) mode = "Stop";
		return this.displayID() + mode + " animation " + animName + " on (" + this.target + ")";
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		root.setAttribute("animation", String.valueOf(this.animationID));
		XMLUtils.setAttribute(root, "target", this.target, -1);
		XMLUtils.setAttribute(root, "mode", this.mode.name(), AnimateCutsceneEventMode.PLAY.name());
		return root;
	}

}

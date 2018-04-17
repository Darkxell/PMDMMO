package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.util.XMLUtils;

public class SetStateCutsceneEvent extends CutsceneEvent
{

	public final PokemonSpriteState state;
	public final int target;

	public SetStateCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, cutscene);
		this.target = XMLUtils.getAttribute(xml, "target", -1);
		PokemonSpriteState s = null;
		try
		{
			s = PokemonSpriteState.valueOf(XMLUtils.getAttribute(xml, "state", null));
		} catch (Exception e)
		{}
		this.state = s;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		CutsceneEntity entity = this.cutscene.get(this.target);
		if (entity != null && entity instanceof CutscenePokemon) ((CutscenePokemon) entity).currentState = this.state;
	}

}

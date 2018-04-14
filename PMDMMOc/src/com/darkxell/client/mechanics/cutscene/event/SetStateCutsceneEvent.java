package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
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

}

package com.darkxell.client.mechanics.cutscene.entity;

import org.jdom2.Element;

import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.util.XMLUtils;

public class CutscenePokemon extends CutsceneEntity
{
	public boolean animated;
	public PokemonSpriteState currentState;
	public int pokemonid;

	public CutscenePokemon(Element xml)
	{
		super(xml);
		this.pokemonid = XMLUtils.getAttribute(xml, "pokemonid", 0);
		try
		{
			this.currentState = PokemonSpriteState.valueOf(XMLUtils.getAttribute(xml, "state", null));
		} catch (Exception e)
		{
			this.currentState = PokemonSpriteState.IDLE;
		}
		this.animated = XMLUtils.getAttribute(xml, "animated", true);
	}

}

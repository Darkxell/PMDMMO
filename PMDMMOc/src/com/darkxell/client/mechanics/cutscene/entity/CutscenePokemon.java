package com.darkxell.client.mechanics.cutscene.entity;

import java.util.Random;

import org.jdom2.Element;

import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.XMLUtils;

public class CutscenePokemon extends CutsceneEntity
{
	public boolean animated;
	public PokemonSpriteState currentState;
	public Direction facing;
	private Pokemon instanciated;

	public int pokemonid;

	public CutscenePokemon(Element xml)
	{
		super(xml);
		this.pokemonid = XMLUtils.getAttribute(xml, "pokemonid", 0);
		try
		{
			this.currentState = PokemonSpriteState.valueOf(XMLUtils.getAttribute(xml, "state", "IDLE"));
		} catch (Exception e)
		{
			this.currentState = PokemonSpriteState.IDLE;
		}
		try
		{
			this.facing = Direction.valueOf(XMLUtils.getAttribute(xml, "facing", "South"));
		} catch (Exception e)
		{
			this.facing = Direction.SOUTH;
		}
		this.animated = XMLUtils.getAttribute(xml, "animated", true);

		this.instanciated = PokemonRegistry.find(this.pokemonid).generate(new Random(), 1);
	}

	public Pokemon toPokemon()
	{
		return this.instanciated;
	}

}

package com.darkxell.client.mechanics.cutscene.entity;

import java.util.Random;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.pokemon.CutscenePokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
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
		this(null, xml);
	}

	public CutscenePokemon(Pokemon pokemon)
	{
		if (pokemon != null) this.pokemonid = pokemon == null ? 0 : pokemon.species().compoundID();
		this.currentState = PokemonSpriteState.IDLE;
		this.facing = Direction.SOUTH;
		this.animated = true;
		this.instanciated = pokemon != null ? pokemon : PokemonRegistry.find(this.pokemonid).generate(new Random(), 1);
	}

	/** @param pokemon - Force the use of an already created Pokémon. */
	public CutscenePokemon(Pokemon pokemon, Element xml)
	{
		super(xml);
		try
		{
			if (pokemon != null) this.pokemonid = pokemon.species().compoundID();
			else if (xml.getChild("teammember", xml.getNamespace()) != null)
			{
				pokemon = this.instanciated = Persistance.player.getMember(Integer.parseInt(xml.getChildText("teammember", xml.getNamespace())));
				this.pokemonid = pokemon.species().id;
			} else this.pokemonid = Integer.parseInt(xml.getChildText("pokemonid", xml.getNamespace()));
		} catch (Exception e)
		{
			e.printStackTrace();
			this.pokemonid = 0;
		}
		try
		{
			this.currentState = PokemonSpriteState.valueOf(xml.getChildText("state", xml.getNamespace()));
		} catch (Exception e)
		{
			this.currentState = PokemonSpriteState.IDLE;
		}
		try
		{
			this.facing = Direction.valueOf(XMLUtils.getAttribute(xml, "facing", "South").toUpperCase());
		} catch (Exception e)
		{
			this.facing = Direction.SOUTH;
		}
		this.animated = xml.getChild("animated") != null && !xml.getChildText("animated").equals("false");

		this.instanciated = pokemon != null ? pokemon : PokemonRegistry.find(this.pokemonid).generate(new Random(), 1);
	}

	@Override
	public AbstractRenderer createRenderer()
	{
		PokemonSprite sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(this.pokemonid));
		sprite.setState(this.currentState);
		sprite.setFacingDirection(this.facing);
		CutscenePokemonRenderer renderer = new CutscenePokemonRenderer(this, sprite);
		renderer.setXY(this.xPos, this.yPos);
		return renderer;
	}

	public Pokemon toPokemon()
	{
		return this.instanciated;
	}

}

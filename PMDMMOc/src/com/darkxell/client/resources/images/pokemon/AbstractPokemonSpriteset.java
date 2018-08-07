package com.darkxell.client.resources.images.pokemon;

import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.client.resources.images.RegularSpriteSet;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.util.Direction;

import javafx.util.Pair;

public class AbstractPokemonSpriteset extends RegularSpriteSet
{

	private static int findHeight(Element xml)
	{
		return Integer.parseInt(xml.getChildText("FrameHeight"));
	}

	private static int findWidth(Element xml)
	{
		return Integer.parseInt(xml.getChildText("FrameWidth"));
	}

	public final boolean hasBigShadow;
	public final int pokemonID;
	final HashMap<Integer, PokemonSpriteSequence> sequences;
	final HashMap<Pair<PokemonSpriteState, Direction>, Integer> states;

	protected AbstractPokemonSpriteset(String path, Element xml, int pokemonID)
	{
		super(path, findWidth(xml), findHeight(xml), -1, -1);
		this.pokemonID = pokemonID;

		this.hasBigShadow = this.spriteWidth > 48 || this.spriteHeight > 48;

		this.states = new HashMap<>();
		for (Element e : xml.getChild("AnimGroupTable").getChildren())
		{
			PokemonSpriteState state = PokemonSpriteState.valueOf(e.getAttributeValue("state").toUpperCase());
			for (Element s : e.getChildren())
				this.states.put(new Pair<>(state, Direction.valueOf(s.getAttributeValue("direction").toUpperCase())),
						Integer.parseInt(s.getAttributeValue("sequence")));
		}

		this.sequences = new HashMap<>();
		for (Element e : xml.getChild("AnimSequenceTable").getChildren())
			this.sequences.put(Integer.parseInt(e.getAttributeValue("id")), new PokemonSpriteSequence(this, e));

	}

	public PokemonSpriteSequence getSequence(PokemonSpriteState state, Direction direction)
	{
		return this.sequences.get(this.states.get(new Pair<>(state, direction)));
	}

	public PokemonSpriteFrame getSprite(PokemonSpriteState state, Direction direction, int tick)
	{
		return this.getSequence(state, direction).getSprite(tick);
	}

}

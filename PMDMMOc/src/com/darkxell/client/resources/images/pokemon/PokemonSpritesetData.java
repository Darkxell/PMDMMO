package com.darkxell.client.resources.images.pokemon;

import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.util.Direction;

import javafx.util.Pair;

public class PokemonSpritesetData
{

	public final boolean hasBigShadow;
	public final int id;
	final HashMap<Integer, PokemonSpriteSequence> sequences = new HashMap<>();
	final HashMap<Pair<PokemonSpriteState, Direction>, Integer> states = new HashMap<>();
	public final int spriteWidth, spriteHeight;

	public PokemonSpritesetData(int id, Element xml)
	{
		this.id = id;

		this.spriteWidth = Integer.parseInt(xml.getChildText("FrameWidth"));
		this.spriteHeight = Integer.parseInt(xml.getChildText("FrameHeight"));

		this.hasBigShadow = this.spriteWidth > 48 || this.spriteHeight > 48;

		for (Element e : xml.getChild("AnimGroupTable").getChildren())
		{
			PokemonSpriteState state = PokemonSpriteState.valueOf(e.getAttributeValue("state").toUpperCase());
			for (Element s : e.getChildren())
				this.states.put(new Pair<>(state, Direction.valueOf(s.getAttributeValue("direction").toUpperCase())),
						Integer.parseInt(s.getAttributeValue("sequence")));
		}

		for (Element e : xml.getChild("AnimSequenceTable").getChildren())
			this.sequences.put(Integer.parseInt(e.getAttributeValue("id")), new PokemonSpriteSequence(this, e));
	}

}

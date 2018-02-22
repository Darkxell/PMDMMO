package com.darkxell.client.resources.images.pokemon;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.util.Direction;

import javafx.util.Pair;

public class AbstractPokemonSpriteset
{

	public final boolean hasBigShadow;
	public final int pokemonID;
	final HashMap<Integer, PokemonSpriteSequence> sequences;
	public final int spriteHeight;
	final BufferedImage[] sprites;
	public final int spriteWidth;
	final HashMap<Pair<PokemonSpriteState, Direction>, Integer> states;

	protected AbstractPokemonSpriteset(String path, Element xml, int pokemonID)
	{
		this.pokemonID = pokemonID;

		this.spriteWidth = Integer.parseInt(xml.getChildText("FrameWidth"));
		this.spriteHeight = Integer.parseInt(xml.getAttributeValue("FrameHeight"));
		this.hasBigShadow = this.spriteWidth > 48 || this.spriteHeight > 48;
		BufferedImage spriteset = Res.getBase(path);

		int x = 0, y = 0, width = spriteset.getWidth() / this.spriteWidth, height = spriteset.getHeight() / this.spriteHeight;
		this.sprites = new BufferedImage[x * y];
		for (; x < width; ++x)
			for (; y < height; ++height)
				this.sprites[y * width + x] = spriteset.getSubimage(x * this.spriteWidth, y * this.spriteHeight, this.spriteWidth, this.spriteHeight);

		this.states = new HashMap<>();
		for (Element e : xml.getChild("AnimGroupTable").getChildren())
		{
			PokemonSpriteState state = PokemonSpriteState.valueOf(e.getAttributeValue("state").toUpperCase());
			for (Element s : e.getChildren())
				this.states.put(new Pair<>(state, Direction.valueOf(s.getAttributeValue("direction"))), Integer.parseInt(s.getAttributeValue("sequence")));
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

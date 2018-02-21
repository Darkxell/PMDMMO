package com.darkxell.client.resources.images.pokemon;

import java.awt.image.BufferedImage;

import org.jdom2.Element;

import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;

public class AbstractPokemonSpriteset
{

	private static final String[] xmlElementNames = new String[] { "idle", "move", "attack", "attack2", "spe1", "spe2", "sleep", "hurt", "rest", "wake",
			"victory", "eating" };

	public final int gravityX;
	public final int gravityY;
	public final boolean hasBigShadow;
	public final int pokemonID;
	public final int spriteHeight;
	private BufferedImage sprites;
	public final int spriteWidth;
	final PokemonSpritesetState[] states;

	protected AbstractPokemonSpriteset(String path, Element xml, int pokemonID)
	{
		this.pokemonID = pokemonID;
		if (xml.getAttribute("size") != null) this.spriteWidth = this.spriteHeight = Integer.parseInt(xml.getAttributeValue("size"));
		else
		{
			this.spriteWidth = Integer.parseInt(xml.getAttributeValue("width"));
			this.spriteHeight = Integer.parseInt(xml.getAttributeValue("height"));
		}
		this.gravityX = XMLUtils.getAttribute(xml, "x", this.defaultX());
		this.gravityY = XMLUtils.getAttribute(xml, "y", this.defaultY());
		this.hasBigShadow = XMLUtils.getAttribute(xml, "bigshadow", false);
		if (this.gravityX == -1 || this.gravityY == -1)
			Logger.e("AbstractPokemonSpriteset(): There is no default gravity coordinates for this Sprite's dimension.");
		this.sprites = Res.getBase(path);

		this.states = new PokemonSpritesetState[11];
		for (int i = 0; i < this.states.length; ++i)
		{
			if (xml.getChild(xmlElementNames[i], xml.getNamespace()) != null)
				this.states[i] = new PokemonSpritesetState(xml.getChild(xmlElementNames[i], xml.getNamespace()));
			else if (i == PokemonSpriteState.ATTACK2.id) this.states[i] = this.states[PokemonSpriteState.ATTACK.id];
			else if (i == PokemonSpriteState.SPECIAL2.id) this.states[i] = this.states[PokemonSpriteState.SPECIAL.id];
			else if (i == PokemonSpriteState.HURT.id) this.states[i] = new PokemonSpritesetState(new int[] { 2 }, new int[] { 25 });
			else if (i == PokemonSpriteState.SLEEP.id || i == PokemonSpriteState.REST.id)
				this.states[i] = new PokemonSpritesetState(new int[] { 0, 1 }, new int[] { 30, 10 });
			else this.states[i] = new PokemonSpritesetState(0);
		}
	}

	private int defaultX()
	{
		if (this.spriteWidth != this.spriteHeight) return 0;
		switch (this.spriteWidth)
		{
			case 32:
				return 16;
			case 48:
				return 24;
			case 64:
				return 32;
			case 96:
				return 48;
			default:
				return -1;
		}
	}

	private int defaultY()
	{
		if (this.spriteWidth != this.spriteHeight) return 0;
		switch (this.spriteWidth)
		{
			case 32:
				return 23;
			case 48:
				return 38;
			case 64:
				return 58;
			case 96:
				return 80;
			default:
				return -1;
		}
	}

	public BufferedImage getSprite(PokemonSpriteState state, Direction facing, int variant)
	{
		int x = 0, y = 0;
		if (state != PokemonSpriteState.SLEEP) y += facing.index();
		x = this.states[state.id].indexes[variant];
		return this.sprites.getSubimage(x * this.spriteWidth, y * this.spriteHeight, this.spriteWidth, this.spriteHeight);
	}

}

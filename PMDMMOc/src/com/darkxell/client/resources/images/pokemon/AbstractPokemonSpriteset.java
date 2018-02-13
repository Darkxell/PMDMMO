package com.darkxell.client.resources.images.pokemon;

import java.awt.image.BufferedImage;

import org.jdom2.Element;

import com.darkxell.client.resources.Res;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;

public class AbstractPokemonSpriteset
{

	public static final byte FACING_E = 2;
	public static final byte FACING_N = 0;
	public static final byte FACING_NE = 1;
	public static final byte FACING_NW = 7;
	public static final byte FACING_S = 4;
	public static final byte FACING_SE = 3;
	public static final byte FACING_SW = 5;
	public static final byte FACING_W = 6;

	private static final String[] xmlElementNames = new String[] { "idle", "move", "attack", "spe1", "spe2", "sleep", "hurt", "rest", "wake", "victory",
			"eating" };

	public final int gravityX;
	public final int gravityY;
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
		if (this.gravityX == -1 || this.gravityY == -1)
			Logger.e("AbstractPokemonSpriteset(): There is no default gravity coordinates for this Sprite's dimension.");
		this.sprites = Res.getBase(path);

		this.states = new PokemonSpritesetState[11];
		for (int i = 0; i < this.states.length; ++i)
		{
			if (xml.getChild(xmlElementNames[i], xml.getNamespace()) != null)
				this.states[i] = new PokemonSpritesetState(xml.getChild(xmlElementNames[i], xml.getNamespace()));
			else if (i == PokemonSprite.STATE_SPECIAL2) this.states[i] = this.states[PokemonSprite.STATE_SPECIAL1];
			else if (i == PokemonSprite.STATE_HURT) this.states[i] = new PokemonSpritesetState(new int[] { 2 }, new int[] { 25 });
			else if (i == PokemonSprite.STATE_SLEEP || i == PokemonSprite.STATE_REST)
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
				return 15;
			case 48:
				return 24;
			case 64:
				return 33;
			case 96:
				return 51;
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
				return 33;
			case 64:
				return 43;
			case 96:
				return 63;
			default:
				return -1;
		}
	}

	public BufferedImage getSprite(byte state, byte facing, int variant)
	{
		int x = 0, y = 0;
		if (state != PokemonSprite.STATE_SLEEP) y += facing;
		x = this.states[state].indexes[variant];
		return this.sprites.getSubimage(x * this.spriteWidth, y * this.spriteHeight, this.spriteWidth, this.spriteHeight);
	}

}

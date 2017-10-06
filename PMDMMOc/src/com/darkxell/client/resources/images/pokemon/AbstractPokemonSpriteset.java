package com.darkxell.client.resources.images.pokemon;

import java.awt.image.BufferedImage;

import org.jdom2.Element;

import com.darkxell.client.resources.Res;

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

	private static final int[][] stateOffsets = new int[][]
	{
	{ 0, 2 },
	{ 4, 2 },
	{ 8, 2 },
	{ 0, 10 },
	{ 8, 10 },
	{ 0, 0 },
	{ 0, 1 },
	{ 0, 18 },
	{ 2, 18 },
	{ 2, 19 },
	{ 5, 19 } };
	private static final String[] xmlElementNames = new String[]
	{ "idle", "move", "attack", "spe1", "spe2", "sleep", "hurt", "rest", "wake", "victory", "eating" };

	public final int gravityX;
	public final int gravityY;
	public final int spriteHeight;
	private BufferedImage sprites;
	public final int spriteWidth;
	final PokemonSpritesetState[] states;
	public final int pokemonID;
	
	protected AbstractPokemonSpriteset(String path, Element xml,int pokemonID)
	{
		this.pokemonID = pokemonID;
		if (xml.getAttribute("size") != null) this.spriteWidth = this.spriteHeight = Integer.parseInt(xml.getAttributeValue("size"));
		else
		{
			this.spriteWidth = Integer.parseInt(xml.getAttributeValue("width"));
			this.spriteHeight = Integer.parseInt(xml.getAttributeValue("height"));
		}
		this.gravityX = Integer.parseInt(xml.getAttributeValue("x"));
		this.gravityY = Integer.parseInt(xml.getAttributeValue("y"));
		this.sprites = Res.getBase(path);

		this.states = new PokemonSpritesetState[11];
		for (int i = 0; i < this.states.length; ++i)
			if (xml.getChild(xmlElementNames[i]) != null) this.states[i] = new PokemonSpritesetState(xml.getChild(xmlElementNames[i]));
			else if (i == PokemonSprite.STATE_HURT) this.states[i] = new PokemonSpritesetState(new int[]
			{ 0 }, new int[]
			{ 25 });
			else if (i == PokemonSprite.STATE_SLEEP || i == PokemonSprite.STATE_REST) this.states[i] = new PokemonSpritesetState(new int[]
			{ 0, 1 }, new int[]
			{ 30, 10 });
			else if (i == PokemonSprite.STATE_EATING) this.states[i] = new PokemonSpritesetState(new int[]
			{ 0, 1 }, new int[]
			{ 10, 10 });
			else this.states[i] = new PokemonSpritesetState(0);
	}

	public BufferedImage getSprite(byte state, byte facing, int variant)
	{
		int x = 0, y = 0;
		if (state == PokemonSprite.STATE_HURT) x += facing;
		else if (state != PokemonSprite.STATE_SLEEP) y += facing;
		x += variant;
		x += stateOffsets[state][0];
		y += stateOffsets[state][1];
		return this.sprites.getSubimage(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight);
	}

}

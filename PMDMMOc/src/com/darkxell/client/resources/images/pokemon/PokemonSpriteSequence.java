package com.darkxell.client.resources.images.pokemon;

import org.jdom2.Element;

import com.darkxell.common.util.XMLUtils;

public class PokemonSpriteSequence
{

	public final int duration;
	final PokemonSpriteFrame[] frames;
	public final int rushPoint, hitPoint, returnPoint;

	public PokemonSpriteSequence(AbstractPokemonSpriteset spriteset, Element xml)
	{
		this.rushPoint = XMLUtils.getAttribute(xml, "rush", 0);
		this.hitPoint = XMLUtils.getAttribute(xml, "hit", 0);
		this.returnPoint = XMLUtils.getAttribute(xml, "return", 0);
		
		this.frames = new PokemonSpriteFrame[xml.getChildren().size()];
		int i = 0;
		for (Element e : xml.getChildren())
		{
			this.frames[i] = new PokemonSpriteFrame(spriteset, e);
			++i;
		}
		this.duration = this.totalDuration();
	}

	public int duration(int tick)
	{
		return this.frames[this.getFrameIndex(tick)].duration;
	}

	private int getFrameIndex(int tick)
	{
		int i = 0;
		while (tick > 0)
		{
			tick -= this.frames[i].duration;
			++i;
		}
		return i;
	}

	public PokemonSpriteFrame getSprite(int tick)
	{
		return this.frames[this.getFrameIndex(tick)];
	}

	private int totalDuration()
	{
		int d = 0;
		for (PokemonSpriteFrame f : this.frames)
			d += f.duration;
		return d;
	}

}

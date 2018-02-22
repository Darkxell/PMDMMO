package com.darkxell.client.resources.images.pokemon;

import java.awt.image.BufferedImage;

import org.jdom2.Element;

import com.darkxell.common.util.XMLUtils;

public class PokemonSpriteFrame
{

	/** Duration of this Frame. */
	public final int duration;
	/** ID of the Frame to use. */
	public final int frameID;
	/** True if the sprite should be flipped horizontally. */
	public final boolean isFlipped;
	/** Offset to apply to the shadow. */
	public final int shadowX, shadowY;
	public final AbstractPokemonSpriteset spriteset;
	/** Offset to apply to the sprite. */
	public final int spriteX, spriteY;

	public PokemonSpriteFrame(AbstractPokemonSpriteset spriteset, Element xml)
	{
		this.spriteset = spriteset;
		this.frameID = XMLUtils.getAttribute(xml, "sprite", 0);
		this.duration = XMLUtils.getAttribute(xml, "duration", 0) / 2;
		this.spriteX = XMLUtils.getAttribute(xml, "spritex", 0);
		this.spriteY = XMLUtils.getAttribute(xml, "spritey", 0);
		this.shadowX = XMLUtils.getAttribute(xml, "shadowx", 0);
		this.shadowY = XMLUtils.getAttribute(xml, "shadowy", 0);
		this.isFlipped = XMLUtils.getAttribute(xml, "flip", false);
	}

	public BufferedImage getSprite()
	{
		return this.spriteset.sprites[this.frameID];
	}

}

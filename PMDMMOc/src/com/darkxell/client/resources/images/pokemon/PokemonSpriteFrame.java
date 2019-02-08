package com.darkxell.client.resources.images.pokemon;

import org.jdom2.Element;

import com.darkxell.common.util.XMLUtils;

public class PokemonSpriteFrame {

    /** Duration of this Frame. */
    public final int duration;
    /** ID of the Frame to use. */
    public final int frameID;
    /** True if the sprite should be flipped horizontally. */
    public final boolean isFlipped;
    /** Offset to apply to the shadow. */
    public final int shadowX, shadowY;
    public final PokemonSpritesetData spriteset;
    /** Offset to apply to the sprite. */
    public final int spriteX, spriteY;

    public PokemonSpriteFrame() {
        this.spriteset = null;
        this.frameID = this.duration = this.spriteX = this.spriteY = this.shadowX = this.shadowY = 0;
        this.isFlipped = false;
    }

    public PokemonSpriteFrame(PokemonSpritesetData pokemonSpriteset, Element xml) {
        this.spriteset = pokemonSpriteset;
        this.frameID = XMLUtils.getAttribute(xml, "sprite", 0);
        this.duration = XMLUtils.getAttribute(xml, "duration", 0);
        this.spriteX = XMLUtils.getAttribute(xml, "spritex", 0);
        this.spriteY = XMLUtils.getAttribute(xml, "spritey", 0);
        this.shadowX = XMLUtils.getAttribute(xml, "shadowx", 0);
        this.shadowY = XMLUtils.getAttribute(xml, "shadowy", 0);
        this.isFlipped = XMLUtils.getAttribute(xml, "flip", false);
    }

    public PokemonSpriteFrame(PokemonSpritesetData spriteset, int frameID, int duration, int spriteX, int spriteY,
            int shadowX, int shadowY, boolean isFlipped) {
        this.spriteset = spriteset;
        this.frameID = frameID;
        this.duration = duration;
        this.spriteX = spriteX;
        this.spriteY = spriteY;
        this.shadowX = shadowX;
        this.shadowY = shadowY;
        this.isFlipped = isFlipped;
    }

    @Override
    public String toString() {
        String s = this.frameID + " for " + this.duration + " ticks";
        if (this.spriteX != 0)
            s += ", X=" + this.spriteX;
        if (this.spriteY != 0)
            s += ", Y=" + this.spriteY;
        if (this.isFlipped)
            s += ", flipped";
        return s;
    }

    public Element toXML() {
        Element root = new Element("AnimFrame");
        XMLUtils.setAttribute(root, "sprite", this.frameID, 0);
        XMLUtils.setAttribute(root, "duration", this.duration, 0);
        XMLUtils.setAttribute(root, "spritex", this.spriteX, 0);
        XMLUtils.setAttribute(root, "spritey", this.spriteY, 0);
        XMLUtils.setAttribute(root, "shadowx", this.shadowX, 0);
        XMLUtils.setAttribute(root, "shadowy", this.shadowY, 0);
        XMLUtils.setAttribute(root, "flip", this.isFlipped, false);
        return root;
    }

}

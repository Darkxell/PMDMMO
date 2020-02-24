package com.darkxell.client.model.pokemonspriteset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AnimFrame")
@XmlAccessorType(XmlAccessType.FIELD)
public class PokemonAnimationFrameModel {

    @XmlAttribute(name = "sprite")
    public int frameID;

    @XmlAttribute
    public int duration;

    @XmlAttribute(name = "spritex")
    public int spriteX;

    @XmlAttribute(name = "spritey")
    public int spriteY;

    @XmlAttribute(name = "shadowx")
    public int shadowX;

    @XmlAttribute(name = "shadowy")
    public int shadowY;

    @XmlAttribute(name = "flip")
    public boolean isFlipped = false;

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

}

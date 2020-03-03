package com.darkxell.client.model.pokemonspriteset;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AnimSequence")
@XmlAccessorType(XmlAccessType.FIELD)
public class PokemonAnimationSequenceModel implements Comparable<PokemonAnimationSequenceModel> {

    @XmlAttribute
    public int id;

    @XmlAttribute
    public int rush = 0;

    @XmlAttribute
    public int hit = 0;

    @XmlAttribute(name = "return")
    public int returnTime = 0;

    @XmlElement(name = "AnimFrame")
    public ArrayList<PokemonAnimationFrameModel> frames;

    @Override
    public int compareTo(PokemonAnimationSequenceModel o) {
        return Integer.compare(this.id, o.id);
    }

}

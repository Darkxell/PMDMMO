package com.darkxell.client.model.pokemonspriteset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AnimSequenceIndex")
@XmlAccessorType(XmlAccessType.FIELD)
public class PokemonAnimationSequenceIndex {

    @XmlAttribute
    public int sequence;

    @XmlAttribute
    public String direction;

}

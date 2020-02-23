package com.darkxell.client.model.pokemonspriteset;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AnimGroup")
@XmlAccessorType(XmlAccessType.FIELD)
public class PokemonAnimationGroupModel implements Comparable<PokemonAnimationGroupModel> {

    @XmlAttribute
    public String state;

    @XmlElement(name = "AnimSequenceIndex")
    public ArrayList<PokemonAnimationSequenceIndex> indexes = new ArrayList<>();

    @Override
    public int compareTo(PokemonAnimationGroupModel o) {
        return this.state.compareTo(o.state);
    }

}

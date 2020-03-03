package com.darkxell.client.model.pokemonspriteset;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AnimData")
@XmlAccessorType(XmlAccessType.FIELD)
public class PokemonAnimationModel {

    @XmlAttribute
    public boolean bigshadow = false;

    @XmlElement(name = "FrameWidth")
    public int width;

    @XmlElement(name = "FrameHeight")
    public int height;

    @XmlElement(name = "AnimGroup")
    @XmlElementWrapper(name = "AnimGroupTable")
    public ArrayList<PokemonAnimationGroupModel> groups;

    @XmlElement(name = "AnimSequence")
    @XmlElementWrapper(name = "AnimSequenceTable")
    public ArrayList<PokemonAnimationSequenceModel> sequences;

    public PokemonAnimationModel() {
    }

}

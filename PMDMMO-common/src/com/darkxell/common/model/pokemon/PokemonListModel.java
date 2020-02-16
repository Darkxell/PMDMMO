package com.darkxell.common.model.pokemon;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pokemon")
@XmlAccessorType(XmlAccessType.FIELD)
public class PokemonListModel {

    @XmlElement(name = "species")
    public ArrayList<PokemonSpeciesModel> pokemon = new ArrayList<>();

    public PokemonListModel copy() {
        PokemonListModel clone = new PokemonListModel();
        for (PokemonSpeciesModel pokemon : this.pokemon)
            clone.pokemon.add(pokemon.copy());
        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PokemonListModel))
            return false;
        PokemonListModel o = (PokemonListModel) obj;
        if (this.pokemon.size() != o.pokemon.size())
            return false;
        for (int i = 0; i < pokemon.size(); ++i) {
            if (!this.pokemon.get(i).equals(o.pokemon.get(i)))
                return false;
        }
        return true;
    }

}

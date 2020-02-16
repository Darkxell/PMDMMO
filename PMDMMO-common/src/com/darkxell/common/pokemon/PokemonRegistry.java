package com.darkxell.common.pokemon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import com.darkxell.common.model.io.ModelIOHandlers;
import com.darkxell.common.model.pokemon.PokemonListModel;
import com.darkxell.common.model.pokemon.PokemonSpeciesModel;
import com.darkxell.common.registry.Registry;
import com.darkxell.common.zones.FriendArea;

/**
 * Holds all Pokemon species.
 */
public final class PokemonRegistry extends Registry<PokemonSpecies, PokemonListModel> {

    public PokemonRegistry(URL registryURL) throws IOException {
        super(registryURL, ModelIOHandlers.pokemon, "Species", 0);
        FriendArea.computeMaxFriends(this);
    }

    @Override
    protected HashMap<Integer, PokemonSpecies> deserializeDom(PokemonListModel model) {
        HashMap<Integer, PokemonSpecies> speciesMap = new HashMap<>();

        for (PokemonSpeciesModel species : model.pokemon) {
            speciesMap.put(species.getID(), new PokemonSpecies(species));
        }

        return speciesMap;
    }

    /**
     * Maps forms to species, if any.
     *
     * @return Main form of the species, if it is multi-form.
     */
    public PokemonSpecies parentSpecies(PokemonSpecies form) {
        return find(form.getFormOf());
    }

    @Override
    protected PokemonListModel serializeDom(HashMap<Integer, PokemonSpecies> speciesList) {
        PokemonListModel model = new PokemonListModel();
        speciesList.values().forEach(s -> model.pokemon.add(s.getModel()));
        model.pokemon.sort(Comparator.naturalOrder());
        return model;
    }

    @Override
    public ArrayList<PokemonSpecies> toList() {
        ArrayList<PokemonSpecies> list = super.toList();
        list.removeIf(s -> s.getID() == 0);
        return list;
    }
}

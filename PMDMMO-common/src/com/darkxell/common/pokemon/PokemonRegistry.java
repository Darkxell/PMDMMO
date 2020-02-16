package com.darkxell.common.pokemon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Element;

import com.darkxell.common.model.pokemon.PokemonSpeciesModel;
import com.darkxell.common.registry.Registry;
import com.darkxell.common.zones.FriendArea;

/**
 * Holds all Pokemon species.
 */
public final class PokemonRegistry extends Registry<PokemonSpecies> {
    /**
     * Quick-lookup map of forms to main species ids.
     */
    private HashMap<Integer, Integer> forms = new HashMap<>();

    public PokemonRegistry(URL registryURL) throws IOException {
        super(registryURL, "Species", 0);
        this.registerForms();
        FriendArea.computeMaxFriends(this);
    }

    protected HashMap<Integer, PokemonSpecies> deserializeDom(Element root) {
        List<Element> speciesElements = root.getChildren("pokemon", root.getNamespace());
        HashMap<Integer, PokemonSpecies> speciesMap = new HashMap<>(speciesElements.size());
        for (Element e : speciesElements) {
            PokemonSpecies species = new PokemonSpecies(e);
            speciesMap.put(species.getID(), species);
            
            // Temporary hacks to fix old implementation problems
            species.getModel().getBaseStats().get(0).setMoveSpeed(1);
            for (PokemonSpeciesModel form : species.getModel().getForms()) {
                form.getBaseStats().get(0).setMoveSpeed(1);
                form.setForms(new ArrayList<>());
            }
        }
        return speciesMap;
    }

    /**
     * Maps forms to species, if any.
     *
     * @return Main form of the species, if it is multi-form.
     */
    public PokemonSpecies parentSpecies(PokemonSpecies form) {
        if (!forms.containsKey(form.getID()))
            return null;
        return find(forms.get(form.getID()));
    }

    /**
     * Generate forms from current main-form species.
     */
    private void registerForms() {
        // copy cache values to avoid concurrent access errors
        for (PokemonSpecies s : new ArrayList<>(this.cache.values()))
            for (PokemonSpecies form : s.forms()) {
                this.cache.put(form.getID(), form);
                this.forms.put(form.getID(), s.getID());
            }
    }

    protected Element serializeDom(HashMap<Integer, PokemonSpecies> speciesList) {
        Element xml = new Element("pokemon");
        // did you know the plural of species is species? incredible innit
        for (PokemonSpecies species : speciesList.values())
            // only serialize main-form species
            if (species.getFormID() == 0)
                xml.addContent(species.toXML());
        return xml;
    }

    @Override
    public ArrayList<PokemonSpecies> toList() {
        ArrayList<PokemonSpecies> list = super.toList();
        list.removeIf(s -> s.getID() == 0);
        return list;
    }
}

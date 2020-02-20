package com.darkxell.client.resources.image.pokemon.body;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

import com.darkxell.client.model.pokemonspriteset.PokemonAnimationGroupModel;
import com.darkxell.client.model.pokemonspriteset.PokemonAnimationModel;
import com.darkxell.client.model.pokemonspriteset.PokemonAnimationSequenceIndex;
import com.darkxell.client.model.pokemonspriteset.PokemonAnimationSequenceModel;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Pair;

public class PokemonSpritesetData {

    public final boolean hasBigShadow;
    public final int id;
    final HashMap<Integer, PSDSequence> sequences;
    public final int spriteWidth, spriteHeight;
    final HashMap<Pair<PokemonSpriteState, Direction>, Integer> states;

    public PokemonSpritesetData(int id, boolean hasBigShadow, int spriteWidth, int spriteHeight,
            HashMap<Pair<PokemonSpriteState, Direction>, Integer> states, HashMap<Integer, PSDSequence> sequences) {
        this.id = id;
        this.hasBigShadow = hasBigShadow;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;

        this.states = new HashMap<>(states);
        this.sequences = new HashMap<>(sequences);
    }

    public PokemonSpritesetData(int id, PokemonAnimationModel model) {
        this.id = id;

        this.spriteWidth = model.width;
        this.spriteHeight = model.height;
        this.hasBigShadow = model.bigshadow;

        this.states = new HashMap<>();
        this.sequences = new HashMap<>();

        for (PokemonAnimationGroupModel group : model.groups) {
            PokemonSpriteState state = PokemonSpriteState.valueOf(group.state.toUpperCase());
            for (PokemonAnimationSequenceIndex index : group.indexes)
                this.states.put(new Pair<>(state, Direction.valueOf(index.direction.toUpperCase())), index.sequence);
        }

        for (PokemonAnimationSequenceModel sequence : model.sequences)
            this.sequences.put(sequence.id, new PSDSequence(this, sequence));
    }

    public PokemonSpritesetData(Integer id) {
        this.id = id;
        this.spriteWidth = this.spriteHeight = 16;
        this.hasBigShadow = false;
        this.states = new HashMap<>();
        this.sequences = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public HashMap<Pair<PokemonSpriteState, Direction>, Integer> sequenceMapper() {
        return (HashMap<Pair<PokemonSpriteState, Direction>, Integer>) this.states.clone();
    }

    public Collection<PSDSequence> sequences() {
        return this.sequences.values();
    }

    @Override
    public String toString() {
        String value = this.id + "- ";
        PokemonSpecies species = Registries.species().find(this.id);
        if (species != null)
            value += species;
        return value;
    }

    public PokemonAnimationModel toModel() {
        PokemonAnimationModel model = new PokemonAnimationModel();
        model.bigshadow = this.hasBigShadow;
        model.height = this.spriteHeight;
        model.width = this.spriteWidth;

        model.sequences = new ArrayList<>();
        this.sequences.values().forEach(s -> model.sequences.add(s.model));
        model.sequences.sort(Comparator.naturalOrder());

        model.groups = new ArrayList<>();
        for (Pair<PokemonSpriteState, Direction> state : this.states.keySet()) {
            PokemonAnimationGroupModel group = new PokemonAnimationGroupModel();
            group.state = state.first.name().toLowerCase();
            for (PokemonAnimationGroupModel g : model.groups)
                if (g.state.equals(state.first.name().toLowerCase())) {
                    group = g;
                    break;
                }
            PokemonAnimationSequenceIndex seq = new PokemonAnimationSequenceIndex();
            seq.direction = state.second.lowercaseName();
            seq.sequence = this.states.get(state);
            group.indexes.add(seq);
            model.groups.add(group);
        }
        model.groups.sort(Comparator.naturalOrder());

        return model;
    }

}

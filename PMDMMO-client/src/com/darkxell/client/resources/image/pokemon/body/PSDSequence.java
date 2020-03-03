package com.darkxell.client.resources.image.pokemon.body;

import java.util.ArrayList;
import java.util.Collection;

import com.darkxell.client.model.pokemonspriteset.PokemonAnimationFrameModel;
import com.darkxell.client.model.pokemonspriteset.PokemonAnimationSequenceModel;

/**
 * Pokemon Spriteset Data Sequence
 */
public class PSDSequence implements Comparable<PSDSequence> {

    public final int duration;
    public final PokemonAnimationSequenceModel model;

    public PSDSequence(Integer id) {
        this.model = new PokemonAnimationSequenceModel();
        this.model.id = id;
        this.duration = 0;
        this.model.frames = new ArrayList<>();
        this.model.rush = this.model.hit = this.model.returnTime = 0;
    }

    public PSDSequence(Integer id, int rush, int hit, int ret, Collection<PokemonAnimationFrameModel> frames) {
        this.model = new PokemonAnimationSequenceModel();
        this.model.id = id;
        this.model.rush = rush;
        this.model.hit = hit;
        this.model.returnTime = ret;
        this.model.frames = new ArrayList<>(frames);
        this.duration = this.totalDuration();
    }

    public PSDSequence(PokemonSpritesetData pokemonSpritesetData, PokemonAnimationSequenceModel model) {
        this.model = model;
        this.duration = this.totalDuration();
    }

    @Override
    public int compareTo(PSDSequence o) {
        return Integer.compare(this.getID(), o.getID());
    }

    public int getID() {
        return this.model.id;
    }

    public double dashOffset(int tick) {
        if (tick <= this.model.rush || tick >= this.model.returnTime)
            return 0;
        if (tick == this.model.hit)
            return 1;
        if (tick <= this.model.hit)
            return (tick - this.model.rush) * 1. / (this.model.hit - this.model.rush);
        return 1 - ((tick - this.model.hit) * 1. / (this.model.returnTime - this.model.hit));
    }

    public Collection<PokemonAnimationFrameModel> frames() {
        return this.model.frames;
    }

    public PokemonAnimationFrameModel getFrame(int tick) {
        return this.model.frames.get(this.getFrameIndex(tick));
    }

    private int getFrameIndex(int tick) {
        int i = -1;
        while (tick >= 0) {
            ++i;
            tick -= this.model.frames.get(i).duration;
        }
        return i;
    }

    @Override
    public String toString() {
        return String.valueOf(this.model.id);
    }

    private int totalDuration() {
        int d = 0;
        for (PokemonAnimationFrameModel f : this.model.frames)
            d += f.duration;
        return d;
    }

}

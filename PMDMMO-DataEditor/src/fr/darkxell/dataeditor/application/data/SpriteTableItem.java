package fr.darkxell.dataeditor.application.data;

import java.util.HashSet;

import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;

public class SpriteTableItem {

    public int north = -1, northeast = -1, east = -1, southeast = -1, south = -1, southwest = -1, west = -1,
            northwest = -1;
    public final PokemonSpriteState state;

    public SpriteTableItem(PokemonSpriteState state) {
        super();
        this.state = state;
    }

    public void fillWithNew(HashSet<Integer> existing) {
        int i = 0;
        while (existing.contains(i))
            ++i;

        if (this.north == -1) {
            this.north = i;
            do
                ++i;
            while (existing.contains(i));
        }

        if (this.northeast == -1) {
            this.northeast = i;
            do
                ++i;
            while (existing.contains(i));
        }

        if (this.east == -1) {
            this.east = i;
            do
                ++i;
            while (existing.contains(i));
        }

        if (this.southeast == -1) {
            this.southeast = i;
            do
                ++i;
            while (existing.contains(i));
        }

        if (this.south == -1) {
            this.south = i;
            do
                ++i;
            while (existing.contains(i));
        }

        if (this.southwest == -1) {
            this.southwest = i;
            do
                ++i;
            while (existing.contains(i));
        }

        if (this.west == -1) {
            this.west = i;
            do
                ++i;
            while (existing.contains(i));
        }

        if (this.northwest == -1)
            this.northwest = i;
    }

    public String getEast() {
        return this.east == -1 ? "N/A" : String.valueOf(this.east);
    }

    public String getNorth() {
        return this.north == -1 ? "N/A" : String.valueOf(this.north);
    }

    public String getNortheast() {
        return this.northeast == -1 ? "N/A" : String.valueOf(this.northeast);
    }

    public String getNorthwest() {
        return this.northwest == -1 ? "N/A" : String.valueOf(this.northwest);
    }

    public String getSouth() {
        return this.south == -1 ? "N/A" : String.valueOf(this.south);
    }

    public String getSoutheast() {
        return this.southeast == -1 ? "N/A" : String.valueOf(this.southeast);
    }

    public String getSouthwest() {
        return this.southwest == -1 ? "N/A" : String.valueOf(this.southwest);
    }

    public PokemonSpriteState getState() {
        return this.state;
    }

    public String getWest() {
        return this.west == -1 ? "N/A" : String.valueOf(this.west);
    }

    public void reset() {
        this.north = this.northeast = this.east = this.southeast = this.south = this.southwest = this.west = this.northwest = -1;
    }

}

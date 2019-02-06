package com.darkxell.common.dungeon.floor.room;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;

/** Represents a Room in a Floor. */
public class ComplexRoom extends Room {

    /** The parts of this Room. These sub-rooms may collide without problem. */
    private SquareRoom[] parts;

    public ComplexRoom(Floor floor, Element xml) {
        super(floor, xml);
        List<Element> parts = xml.getChildren();
        this.parts = new SquareRoom[parts.size()];
        for (int i = 0; i < this.parts.length; ++i)
            this.parts[i] = new SquareRoom(this.floor, parts.get(i));
    }

    @Override
    public boolean contains(int x, int y) {
        for (SquareRoom r : this.parts)
            if (r.contains(x, y))
                return true;
        return false;
    }

    @Override
    public ArrayList<Tile> listTiles() {
        HashSet<Tile> tiles = new HashSet<>();
        for (SquareRoom r : this.parts)
            tiles.addAll(r.listTiles());
        ArrayList<Tile> sorted = new ArrayList<>(tiles);
        sorted.sort(Comparator.naturalOrder());
        return sorted;
    }

    @Override
    public HashSet<Tile> outline() {
        HashSet<Tile> outline = new HashSet<>();
        for (SquareRoom r : this.parts)
            outline.addAll(r.outline());
        outline.removeIf(this::contains);
        return outline;
    }

    public SquareRoom[] subRooms() {
        return this.parts;
    }
}

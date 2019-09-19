package com.darkxell.common.trap;

import java.util.ArrayList;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.dungeon.TrapSteppedOnEvent;
import com.darkxell.common.registry.Registry;

/**
 * Holds all Traps.
 */
public final class TrapRegistry extends Registry<Trap> {
    public static final Trap WONDER_TILE = new WonderTileTrap(0);

    /**
     * TODO: remove once TrapRegistry is fully implemented
     */
    private static Element generatePlaceholderDocument() {
        return new Element("trap");
    }

    protected Element serializeDom(HashMap<Integer, Trap> element) {
        return generatePlaceholderDocument();
    }

    protected HashMap<Integer, Trap> deserializeDom(Element e) {
        HashMap<Integer, Trap> traps = new HashMap<>();
        for (int i = 1; i < 18; i++)
            traps.put(i, new Trap(i) {
                @Override
                public void onPokemonStep(TrapSteppedOnEvent trapEvent, ArrayList<Event> events) {
                }
            });
        traps.put(WONDER_TILE.id, WONDER_TILE);
        return traps;
    }

    public TrapRegistry() {
        super(generatePlaceholderDocument(), "Traps");
    }
}

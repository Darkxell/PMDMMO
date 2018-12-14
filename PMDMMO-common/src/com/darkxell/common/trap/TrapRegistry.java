package com.darkxell.common.trap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import com.darkxell.common.Registry;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import org.jdom2.Element;

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
        for (int i = 1; i < 18; i++) {
            traps.put(i, new Trap(i) {
                @Override
                public void onPokemonStep(Floor floor, DungeonPokemon pokemon, ArrayList<DungeonEvent> events) {
                }
            });
        }
        return traps;
    }

    public TrapRegistry() {
        super(generatePlaceholderDocument(), "Traps");
    }
}

package com.darkxell.common.dungeon.data;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;

import com.darkxell.common.model.dungeon.DungeonListModel;
import com.darkxell.common.model.dungeon.DungeonModel;
import com.darkxell.common.model.io.ModelIOHandlers;
import com.darkxell.common.registry.Registry;

/**
 * Holds all Dungeons.
 */
public final class DungeonRegistry extends Registry<Dungeon, DungeonListModel> {

    public DungeonRegistry(URL registryURL) throws IOException {
        super(registryURL, ModelIOHandlers.dungeon, "Dungeons");
    }

    @Override
    protected HashMap<Integer, Dungeon> deserializeDom(DungeonListModel model) {
        HashMap<Integer, Dungeon> dungeons = new HashMap<>();
        for (DungeonModel d : model.dungeons) {
            Dungeon dungeon = new Dungeon(d);
            dungeons.put(dungeon.getID(), dungeon);
        }
        return dungeons;
    }

    @Override
    protected DungeonListModel serializeDom(HashMap<Integer, Dungeon> dungeons) {
        DungeonListModel model = new DungeonListModel();
        dungeons.values().forEach(d -> model.dungeons.add(d.getModel()));
        model.dungeons.sort(Comparator.naturalOrder());
        return model;
    }
}

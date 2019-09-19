package com.darkxell.common.dungeon.data;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Element;

import com.darkxell.common.registry.Registry;

/**
 * Holds all Dungeons.
 */
public final class DungeonRegistry extends Registry<Dungeon> {
    protected Element serializeDom(HashMap<Integer, Dungeon> dungeons) {
        Element xml = new Element("dungeons");
        for (Dungeon dungeon : dungeons.values())
            xml.addContent(dungeon.toXML());
        return xml;
    }

    protected HashMap<Integer, Dungeon> deserializeDom(Element root) {
        List<Element> dungeonElements = root.getChildren("dungeon", root.getNamespace());
        HashMap<Integer, Dungeon> dungeons = new HashMap<>(dungeonElements.size());
        for (Element e : dungeonElements) {
            Dungeon dungeon = new Dungeon(e);
            dungeons.put(dungeon.id, dungeon);
        }
        return dungeons;
    }

    public DungeonRegistry(URL registryURL) throws IOException {
        super(registryURL, "Dungeons");
    }
}

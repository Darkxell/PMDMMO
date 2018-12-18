package com.darkxell.common.move;

import com.darkxell.common.Registry;
import org.jdom2.Element;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Holds all Moves.
 */
public final class MoveRegistry extends Registry<Move> {
    public static Move ATTACK, STRUGGLE;

    protected Element serializeDom(HashMap<Integer, Move> dungeons) {
        Element xml = new Element("dungeons");
        for (Move move : dungeons.values()) {
            xml.addContent(move.toXML());
        }
        return xml;
    }

    protected HashMap<Integer, Move> deserializeDom(Element root) {
        List<Element> moveElements = root.getChildren("move", root.getNamespace());
        HashMap<Integer, Move> moves = new HashMap<>(moveElements.size());
        for (Element e : moveElements) {
            Move move = new Move(e);
            moves.put(move.id, move);
        }
        return moves;
    }

    public MoveRegistry(URL registryURL) throws IOException {
        super(registryURL, "Moves");

        ATTACK = this.find(0);
        STRUGGLE = this.find(2002);
    }
}

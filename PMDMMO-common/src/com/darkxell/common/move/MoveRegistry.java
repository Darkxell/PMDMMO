package com.darkxell.common.move;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Element;

import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.move.Move.MoveRange;
import com.darkxell.common.move.Move.MoveTarget;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.registry.Registry;
import com.darkxell.common.util.XMLUtils;

/**
 * Holds all Moves.
 */
public final class MoveRegistry extends Registry<Move> {
    public static Move ATTACK, STRUGGLE;

    public MoveRegistry(URL registryURL) throws IOException {
        super(registryURL, "Moves");

        ATTACK = this.find(0);
        STRUGGLE = this.find(2002);
    }

    private Move createMove(Element xml) {
        MoveBuilder builder = new MoveBuilder().withID(Integer.parseInt(xml.getAttributeValue("id")))
                .withType(PokemonType.find(Integer.parseInt(xml.getAttributeValue("type"))))
                .withCategory(MoveCategory.valueOf(xml.getAttributeValue("category")))
                .withPP(Integer.parseInt(xml.getAttributeValue("pp"))).withPower(XMLUtils.getAttribute(xml, "power", 0))
                .withAccuracy(XMLUtils.getAttribute(xml, "accuracy", 100))
                .withRange(MoveRange.valueOf(XMLUtils.getAttribute(xml, "range", MoveRange.Front.name())))
                .withTargets(MoveTarget.valueOf(XMLUtils.getAttribute(xml, "targets", MoveTarget.Foes.name())))
                .withCritical(XMLUtils.getAttribute(xml, "critical", -1))
                .withEffectID(XMLUtils.getAttribute(xml, "effect", 0));
        if (!XMLUtils.getAttribute(xml, "reflectable", false))
            builder.withoutReflectable();
        if (!XMLUtils.getAttribute(xml, "snatchable", false))
            builder.withoutSnatchable();
        if (XMLUtils.getAttribute(xml, "sound", false))
            builder.withSound();
        if (XMLUtils.getAttribute(xml, "freeze", false))
            builder.withFreezePiercing();
        if (!XMLUtils.getAttribute(xml, "damage", false))
            builder.withoutDamage();
        if (!XMLUtils.getAttribute(xml, "ginsengable", false))
            builder.withoutGinsengable();
        return builder.build();
    }

    protected HashMap<Integer, Move> deserializeDom(Element root) {
        List<Element> moveElements = root.getChildren("move", root.getNamespace());
        HashMap<Integer, Move> moves = new HashMap<>(moveElements.size());
        for (Element e : moveElements) {
            Move move = createMove(e);
            moves.put(move.id, move);
        }
        return moves;
    }

    protected Element serializeDom(HashMap<Integer, Move> dungeons) {
        Element xml = new Element("moves");
        for (Move move : dungeons.values())
            xml.addContent(move.toXML());
        return xml;
    }
}

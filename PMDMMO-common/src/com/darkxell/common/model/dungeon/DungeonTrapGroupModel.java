package com.darkxell.common.model.dungeon;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.jdom2.Element;

import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.trap.Trap;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.XMLUtils.IntegerArrayAdapter;

/** Describes how a Traps appear in a Dungeon. */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DungeonTrapGroupModel {

    public static final String XML_ROOT = "trap";

    /** The weight of each Trap. Represents how likely it is to appear compared to other traps on this floor. */
    @XmlAttribute
    @XmlJavaTypeAdapter(IntegerArrayAdapter.class)
    private Integer[] chances = new Integer[0];

    /** The floors this Trap can appear on. */
    @XmlElement
    private FloorSet floors = null;

    /** The Traps IDs. */
    @XmlAttribute
    @XmlJavaTypeAdapter(IntegerArrayAdapter.class)
    private Integer[] ids = new Integer[0];

    public DungeonTrapGroupModel() {
    }

    public DungeonTrapGroupModel(Element xml) {
        this.ids = XMLUtils.readIntegerArray(xml.getChild("ids", xml.getNamespace()));
        if (xml.getChild("chances", xml.getNamespace()) == null) {
            this.chances = new Integer[this.ids.length];
            for (int i = 0; i < this.chances.length; ++i)
                this.chances[i] = 1;
        } else
            this.chances = XMLUtils.readIntegerArray(xml.getChild("chances", xml.getNamespace()));
        this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT, xml.getNamespace()));
    }

    public DungeonTrapGroupModel(Integer[] ids, Integer[] chances, FloorSet floors) {
        this.ids = ids;
        this.chances = chances;
        this.floors = floors;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DungeonTrapGroupModel))
            return false;
        DungeonTrapGroupModel o = (DungeonTrapGroupModel) obj;
        return this.floors.equals(o.floors) && Arrays.equals(this.ids, o.ids) && Arrays.equals(this.chances, o.chances);
    }

    public Integer[] getChances() {
        return chances.clone();
    }

    public FloorSet getFloors() {
        return floors;
    }

    public Integer[] getIds() {
        return ids.clone();
    }

    public Element toXML() {
        Element root = new Element(XML_ROOT);
        root.addContent(XMLUtils.toXML("ids", this.ids));
        root.addContent(this.floors.toXML());

        boolean chances = false;
        for (int c : this.chances)
            if (c != 1) {
                chances = true;
                break;
            }
        if (chances)
            root.addContent(XMLUtils.toXML("chances", this.chances));

        return root;
    }

    public Trap[] traps() {
        Trap[] traps = new Trap[this.ids.length];
        for (int i = 0; i < traps.length; ++i)
            traps[i] = Registries.traps().find(this.ids[i]);
        return traps;
    }

}

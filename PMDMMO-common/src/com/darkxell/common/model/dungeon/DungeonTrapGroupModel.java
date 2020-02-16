package com.darkxell.common.model.dungeon;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.trap.Trap;
import com.darkxell.common.util.XMLUtils.IntegerArrayAdapter;

/** Describes how a Traps appear in a Dungeon. */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DungeonTrapGroupModel {

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

    public DungeonTrapGroupModel(Integer[] ids, Integer[] chances, FloorSet floors) {
        this.ids = ids;
        this.chances = chances;
        this.floors = floors;
    }

    public DungeonTrapGroupModel copy() {
        return new DungeonTrapGroupModel(ids.clone(), chances.clone(), floors.copy());
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

    public Trap[] traps() {
        Trap[] traps = new Trap[this.ids.length];
        for (int i = 0; i < traps.length; ++i)
            traps[i] = Registries.traps().find(this.ids[i]);
        return traps;
    }

}

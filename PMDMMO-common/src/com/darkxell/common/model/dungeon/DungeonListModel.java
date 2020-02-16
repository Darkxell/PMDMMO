package com.darkxell.common.model.dungeon;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dungeons")
@XmlAccessorType(XmlAccessType.FIELD)
public class DungeonListModel {

    @XmlElement(name = "dungeon")
    public ArrayList<DungeonModel> dungeons = new ArrayList<>();

    public DungeonListModel copy() {
        DungeonListModel clone = new DungeonListModel();
        for (DungeonModel dungeon : this.dungeons)
            clone.dungeons.add(dungeon.copy());
        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DungeonListModel))
            return false;
        DungeonListModel o = (DungeonListModel) obj;
        if (this.dungeons.size() != o.dungeons.size())
            return false;
        for (int i = 0; i < dungeons.size(); ++i) {
            if (!this.dungeons.get(i).equals(o.dungeons.get(i)))
                return false;
        }
        return true;
    }

}

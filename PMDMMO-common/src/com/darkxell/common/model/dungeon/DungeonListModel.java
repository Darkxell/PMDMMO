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

}

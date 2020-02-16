package com.darkxell.common.model.floor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class IStaticFloorRoom {

    @XmlAttribute
    private boolean isMonsterHouse = false;

    public boolean isMonsterHouse() {
        return isMonsterHouse;
    }

}

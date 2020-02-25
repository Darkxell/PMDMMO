package com.darkxell.client.model.cutscene.end;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.LoadFreezoneCutsceneEnd;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.XMLUtils.FreezoneInfoAdapter;
import com.darkxell.common.zones.FreezoneInfo;

@XmlRootElement(name = "loadfreezone")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoadFreezoneCutsceneEndModel extends CutsceneEndModel {

    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(FreezoneInfoAdapter.class)
    private FreezoneInfo freezone;

    @XmlAttribute(name = "xpos")
    private Integer xPos;

    @XmlAttribute(name = "ypos")
    private Integer yPos;

    @XmlAttribute(name = "facing")
    private Direction direction;

    public LoadFreezoneCutsceneEndModel() {
        super(CutsceneEndType.LOAD_FREEZONE);
    }

    public LoadFreezoneCutsceneEndModel(FreezoneInfo freezone, Integer xPos, Integer yPos, Direction direction,
            String functionID, boolean fadesOut) {
        super(CutsceneEndType.LOAD_FREEZONE, functionID, fadesOut);
        this.freezone = freezone;
        this.xPos = xPos;
        this.yPos = yPos;
        this.direction = direction;
    }

    @Override
    protected CutsceneEndModel copyChild() {
        LoadFreezoneCutsceneEndModel clone = new LoadFreezoneCutsceneEndModel();
        clone.setFreezone(this.getFreezone());
        clone.setXPos(this.getXPos());
        clone.setYPos(this.getYPos());
        clone.setDirection(this.getDirection());
        return clone;
    }

    public Direction getDirection() {
        return direction;
    }

    public FreezoneInfo getFreezone() {
        return freezone;
    }

    public Integer getXPos() {
        return xPos;
    }

    public Integer getYPos() {
        return yPos;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setFreezone(FreezoneInfo freezone) {
        this.freezone = freezone;
    }

    public void setXPos(Integer xPos) {
        this.xPos = xPos;
    }

    public void setYPos(Integer yPos) {
        this.yPos = yPos;
    }

    @Override
    public CutsceneEnd build(Cutscene cutscene) {
        return new LoadFreezoneCutsceneEnd(cutscene, this);
    }

}

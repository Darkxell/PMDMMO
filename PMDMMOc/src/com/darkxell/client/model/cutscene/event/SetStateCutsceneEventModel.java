package com.darkxell.client.model.cutscene.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;

@XmlRootElement(name = "setstate")
@XmlAccessorType(XmlAccessType.FIELD)
public class SetStateCutsceneEventModel extends CutsceneEventModel {

    @XmlAttribute
    private int target;

    @XmlAttribute
    private PokemonSpriteState state;

    public SetStateCutsceneEventModel() {
        super(CutsceneEventType.setstate);
    }

    public SetStateCutsceneEventModel(Integer id, int target, PokemonSpriteState state) {
        super(CutsceneEventType.setstate, id);
        this.target = target;
        this.state = state;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        SetStateCutsceneEventModel clone = new SetStateCutsceneEventModel();
        clone.setTarget(this.target);
        clone.setState(this.state);
        return clone;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public PokemonSpriteState getState() {
        return state;
    }

    public void setState(PokemonSpriteState state) {
        this.state = state;
    }

}

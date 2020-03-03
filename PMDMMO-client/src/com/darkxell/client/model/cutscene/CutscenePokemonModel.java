package com.darkxell.client.model.cutscene;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;
import com.darkxell.common.util.Direction;

@XmlRootElement(name = "pokemon")
@XmlAccessorType(XmlAccessType.FIELD)
public class CutscenePokemonModel extends CutsceneEntityModel {

    @XmlElement(name = "teammember")
    private Integer teamMember;

    @XmlElement(name = "pokemonid")
    private Integer pokemonID;

    @XmlElement
    private PokemonSpriteState state;

    @XmlAttribute
    private Direction facing;

    @XmlElement
    private Boolean animated;

    public CutscenePokemonModel() {
    }

    public CutscenePokemonModel(Integer cutsceneid, Double xpos, Double ypos, Integer teamMember, Integer pokemonID,
            PokemonSpriteState state, Direction facing, Boolean animated) {
        super(cutsceneid, xpos, ypos);
        this.teamMember = teamMember;
        this.pokemonID = pokemonID;
        this.state = state;
        this.facing = facing;
        this.animated = animated;
    }

    public Boolean getAnimated() {
        return animated;
    }

    public Direction getFacing() {
        return facing;
    }

    public Integer getPokemonID() {
        return pokemonID;
    }

    public PokemonSpriteState getState() {
        return state;
    }

    public Integer getTeamMember() {
        return teamMember;
    }

    public void setAnimated(Boolean animated) {
        this.animated = animated;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public void setPokemonID(Integer pokemonID) {
        this.pokemonID = pokemonID;
    }

    public void setState(PokemonSpriteState state) {
        this.state = state;
    }

    public void setTeamMember(Integer teamMember) {
        this.teamMember = teamMember;
    }

    @Override
    public CutscenePokemonModel copy() {
        return new CutscenePokemonModel(this.getCutsceneID(), this.getXPos(), this.getYPos(), teamMember, pokemonID,
                state, facing, animated);
    }

}

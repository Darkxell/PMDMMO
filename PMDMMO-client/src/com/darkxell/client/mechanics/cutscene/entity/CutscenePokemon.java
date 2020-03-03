package com.darkxell.client.mechanics.cutscene.entity;

import java.util.Random;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.model.cutscene.CutscenePokemonModel;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.pokemon.CutscenePokemonRenderer;
import com.darkxell.client.resources.image.pokemon.body.PokemonSprite;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpritesets;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.Direction;

public class CutscenePokemon extends CutsceneEntity {
    public boolean animated;
    public PokemonSpriteState currentState;
    public Direction facing;
    public Pokemon instanciated;
    private CutscenePokemonModel model;

    public CutscenePokemon(CutscenePokemonModel model) {
        super(model);
        this.model = model;
        this.animated = model.getAnimated();
        this.currentState = model.getState();
        this.facing = model.getFacing();

        if (model.getTeamMember() != null)
            this.instanciated = Persistence.player.getMember(model.getTeamMember());
        else
            this.instanciated = Registries.species().find(model.getPokemonID()).generate(new Random(), 1, 0);
    }

    @Override
    public AbstractRenderer createRenderer() {
        PokemonSprite sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(this.instanciated));
        sprite.setState(this.currentState);
        sprite.setFacingDirection(this.facing);
        CutscenePokemonRenderer renderer = new CutscenePokemonRenderer(this, sprite);
        renderer.setXY(this.xPos, this.yPos);
        return renderer;
    }

    public Pokemon toPokemon() {
        return this.instanciated;
    }

    @Override
    public String toString() {
        return "(" + this.model.getCutsceneID() + ") " + this.instanciated.species() + " @ X=" + this.xPos + ", Y="
                + this.yPos;
    }

}

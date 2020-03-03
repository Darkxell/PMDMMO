package com.darkxell.client.renderers.pokemon;

import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.resources.image.pokemon.body.PokemonSprite;

/** Renders a Pokemon. This Renderer's Coordinates' units are Tiles. */
public class CutscenePokemonRenderer extends AbstractPokemonRenderer {

    public final CutscenePokemon entity;

    public CutscenePokemonRenderer(CutscenePokemon entity, PokemonSprite sprite) {
        super(sprite);
        this.entity = entity;
    }

    @Override
    public double drawX() {
        return super.drawX() * 8;
    }

    @Override
    public double drawY() {
        return super.drawY() * 8;
    }

    private boolean hasMovementAnimation() {
        for (PokemonAnimation a : this.animations())
            if (a.movement != null)
                return true;
        return false;
    }

    @Override
    public boolean shouldRender(int width, int height) {
        return true;
    }

    @Override
    public void update() {
        if (!this.hasMovementAnimation())
            this.setXY(this.entity.xPos, this.entity.yPos);
        this.sprite.setFacingDirection(this.entity.facing);
        if (this.entity.currentState != this.sprite.getState())
            this.sprite.setState(this.entity.currentState, true);
        this.sprite.setAnimated(this.entity.animated);
        super.update();
    }

}

package com.darkxell.client.renderers.pokemon;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.mechanics.freezones.entities.FriendPokemonEntity;
import com.darkxell.client.resources.image.pokemon.body.PokemonSprite;
import com.darkxell.client.state.freezone.CutsceneState;

/** Renders a Pokemon. This Renderer's Coordinates' units are Tiles. */
public class FreezonePokemonRenderer extends AbstractPokemonRenderer {

    public final FreezoneEntity entity;

    public FreezonePokemonRenderer(FreezoneEntity entity, PokemonSprite sprite) {
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

    @Override
    public boolean shouldRender(int width, int height) {
        if (Persistence.stateManager.getCurrentState() instanceof CutsceneState
                && this.entity instanceof FriendPokemonEntity)
            return false;
        return true;
    }

    @Override
    public void update() {
        this.setXY(this.entity.posX, this.entity.posY);
        super.update();
    }

}

package com.darkxell.client.graphics.renderer;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezone.entity.FreezoneEntity;
import com.darkxell.client.mechanics.freezone.entity.FriendPokemonEntity;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.state.freezone.CutsceneState;

/** Renders a Pokemon. This Renderer's Coordinates' units are Tiles. */
public class FreezonePokemonRenderer extends AbstractPokemonRenderer {

    public final FreezoneEntity entity;

    public FreezonePokemonRenderer(FreezoneEntity entity, PokemonSprite sprite) {
        super(sprite);
        this.entity = entity;
    }

    @Override
    public boolean shouldRender(int width, int height) {
        if (Persistence.stateManager.getCurrentState() instanceof CutsceneState && this.entity instanceof FriendPokemonEntity) return false;
        return true;
    }

    @Override
    public void update() {
        this.setXY(this.entity.getX(), this.entity.getY());
        super.update();
    }

    @Override
    public double x() {
        return super.x() * 8;
    }

    @Override
    public double y() {
        return super.y() * 8;
    }

}

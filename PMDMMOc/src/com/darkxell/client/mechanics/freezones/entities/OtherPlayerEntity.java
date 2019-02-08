package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.mechanics.freezones.entities.renderers.OtherPlayerPokemonRenderer;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;

public class OtherPlayerEntity extends FollowsPointEntity {

    /** The nano timestamp of the last update. */
    public long lastupdate;
    public final String name;
    private int spriteID;

    public OtherPlayerEntity(double x, double y, int spriteID, String name, long timestamp) {
        super(x, y, new PokemonSprite(PokemonSpritesets.getSpriteset(spriteID)));
        this.spriteID = spriteID;
        this.name = name;
        this.destinationX = x;
        this.destinationY = y;
    }

    public void applyServerUpdate(double x, double y, int spriteID) {
        this.destinationX = x;
        this.destinationY = y;
        if (this.spriteID != spriteID) {
            this.spriteID = spriteID;
            PokemonSprite previous = this.sprite;
            this.sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(spriteID));
            this.sprite.copyState(previous);
        }
        this.lastupdate = System.nanoTime();
    }

    @Override
    public AbstractRenderer createRenderer() {
        return new OtherPlayerPokemonRenderer(this, this.sprite);
    }

}

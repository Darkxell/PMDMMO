package com.darkxell.client.mechanics.freezone.entity;

import com.darkxell.client.mechanics.freezone.FreezonePlayer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.common.pokemon.Pokemon;

public class AllyEntity extends FollowsPointEntity {

    public final FreezonePlayer playerToFollow;

    public AllyEntity(double x, double y, Pokemon pokemon, FreezonePlayer playerToFollow) {
        super(x, y, new PokemonSprite(PokemonSpritesets.getSpriteset(pokemon)));
        this.interactive = false;
        this.playerToFollow = playerToFollow;
        this.moveDistance = 2;
        this.sprite().setShadowColor(PokemonSprite.ALLY_SHADOW);
        this.sprite().setFacingDirection(this.playerToFollow.renderer().sprite().getFacingDirection());
    }

    @Override
    public void update() {
        this.destinationX = this.playerToFollow.x;
        this.destinationY = this.playerToFollow.y;
        super.update();
    }

}

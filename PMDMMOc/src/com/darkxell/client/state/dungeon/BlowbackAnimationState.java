package com.darkxell.client.state.dungeon;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.travel.ArcAnimation;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;
import com.darkxell.client.renderers.pokemon.DungeonPokemonRenderer;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.pokemon.BlowbackPokemonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public class BlowbackAnimationState extends AnimationState {
    public static final int DURATION_TILE = 6;

    public final BlowbackPokemonEvent event;
    private AnimationEndListener listener;
    public final DungeonPokemon pokemon;
    private DungeonPokemonRenderer renderer;
    private boolean shouldBounce;
    private int tick, duration;
    private TravelAnimation travel;
    private Tile finalTile;

    public BlowbackAnimationState(DungeonState parent, BlowbackPokemonEvent event, AnimationEndListener listener) {
        super(parent);
        this.event = event;
        this.pokemon = event.pokemon;
        this.travel = new TravelAnimation(event.origin.distanceCoordinates(event.destination()));
        this.tick = 0;
        this.duration = (int) (this.travel.distance() * DURATION_TILE);
        this.listener = listener;
        this.shouldBounce = event.wasHurt();
        this.finalTile = event.destination();
        if (this.shouldBounce)
            this.finalTile = this.event.destination().adjacentTile(this.event.direction.opposite());
    }

    @Override
    public void onStart() {
        super.onStart();
        this.renderer = Persistence.dungeonState.pokemonRenderer.getRenderer(this.pokemon);
        this.renderer.sprite().setFacingDirection(this.event.direction.opposite());
        this.renderer.sprite().setState(PokemonSpriteState.HURT);
        this.renderer.sprite().setAnimated(false);
        this.renderer.registerOffset(this.travel);
    }

    @Override
    public void update() {
        super.update();
        ++this.tick;
        this.travel.update(this.tick * 1. / this.duration);
        if (this.tick == this.duration)
            if (this.shouldBounce) {
                this.tick = 0;
                this.duration = 15;
                this.renderer.unregisterOffset(this.travel);
                this.travel = new ArcAnimation(this.event.destination().distanceCoordinates(this.finalTile));
                this.renderer.registerOffset(this.travel);
                this.renderer.setXY(this.event.destination().x, this.event.destination().y);
                this.shouldBounce = false;
            } else {
                Persistence.dungeonState.setDefaultState();
                this.listener.onAnimationEnd(this.animation);
                this.renderer.unregisterOffset(this.travel);
                this.renderer.setXY(this.finalTile.x, this.finalTile.y);
                this.renderer.sprite().resetToDefaultState();
                this.renderer.sprite().setAnimated(true);
            }
    }

}

package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;
import com.darkxell.client.renderers.pokemon.DungeonPokemonRenderer;
import com.darkxell.client.resources.image.pokemon.body.PokemonSprite;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.action.PokemonTravelEvent;

/** Used for Pokemon travel animations. */
public class PokemonTravelState extends DungeonSubState {

    public static final int DURATION_WALK = 15, DURATION_RUN = 3;

    private TravelAnimation[] animations;
    public final int duration;
    public final boolean faraway;
    public final boolean running;
    private int tick;
    private PokemonTravelEvent[] travels;

    public PokemonTravelState(DungeonState parent, PokemonTravelEvent... travels) {
        super(parent);
        this.travels = travels;
        this.animations = new TravelAnimation[this.travels.length];
        for (int i = 0; i < this.travels.length; i++)
            this.animations[i] = new TravelAnimation(
                    this.travels[i].origin().distanceCoordinates(this.travels[i].destination()));
        this.tick = 0;

        boolean f = true, r = false;
        Tile camera = this.parent.getCameraTile();
        for (PokemonTravelEvent t : travels) {
            if (t.running()) {
                r = true;
                if (!f)
                    break;
            }
            if (camera != null && ((Math.abs(t.origin().x - camera.x) < 10 && Math.abs(t.origin().y - camera.y) < 10)
                    || (Math.abs(t.destination().x - camera.x) < 10 && Math.abs(t.destination().y - camera.y) < 10))) {
                f = false;
                if (r)
                    break;
            }
        }
        this.running = r;
        this.faraway = f;
        this.duration = this.running ? DURATION_RUN : DURATION_WALK;
    }

    @Override
    public void onEnd() {
        super.onEnd();

        if (this.running)
            for (PokemonTravelEvent travel : this.travels)
                Persistence.dungeonState.pokemonRenderer.getRenderer(travel.pokemon()).sprite()
                        .updateTickingSpeed(travel.pokemon());
        for (int i = 0; i < this.travels.length; ++i) {
            Persistence.dungeonState.pokemonRenderer.getSprite(this.travels[i].pokemon()).resetOnAnimationEnd();
            Persistence.dungeonState.pokemonRenderer.getRenderer(this.travels[i].pokemon())
                    .unregisterOffset(this.animations[i]);
        }
    }

    @Override
    public void onKeyPressed(Key key) {
    }

    @Override
    public void onKeyReleased(Key key) {
    }

    @Override
    public void onStart() {
        super.onStart();
        for (int i = 0; i < this.travels.length; ++i) {
            PokemonTravelEvent travel = this.travels[i];
            DungeonPokemonRenderer renderer = Persistence.dungeonState.pokemonRenderer.getRenderer(travel.pokemon());
            renderer.sprite().setState(PokemonSpriteState.MOVE, true);
            travel.pokemon().setFacing(travel.direction());
            if (this.running)
                renderer.sprite().setTickingSpeed(PokemonSprite.QUICKER);
            renderer.registerOffset(this.animations[i]);
        }
    }

    @Override
    public void prerender(Graphics2D g, int width, int height) {
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
    }

    @Override
    public void update() {
        ++this.tick;
        if (this.faraway)
            this.tick = this.duration;

        float completion = this.tick * 1f / this.duration;
        for (int i = 0; i < this.travels.length; ++i) {
            this.animations[i].update(completion);
        }

        if (this.tick >= this.duration) {
            for (PokemonTravelEvent travel : this.travels)
                Persistence.dungeonState.pokemonRenderer.getRenderer(travel.pokemon()).setXY(travel.destination().x,
                        travel.destination().y);
            Persistence.eventProcessor().animateDelayed();
        }
    }
}

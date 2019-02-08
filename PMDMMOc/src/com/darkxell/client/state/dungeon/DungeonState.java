package com.darkxell.client.state.dungeon;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.renderers.floor.DungeonItemsRenderer;
import com.darkxell.client.renderers.floor.FloorRenderer;
import com.darkxell.client.renderers.floor.FloorVisibility;
import com.darkxell.client.renderers.floor.GridRenderer;
import com.darkxell.client.renderers.floor.ShadowRenderer;
import com.darkxell.client.renderers.floor.StaticAnimationsRenderer;
import com.darkxell.client.renderers.pokemon.DungeonPokemonRenderer;
import com.darkxell.client.renderers.pokemon.DungeonPokemonRendererHolder;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.pokemon.DungeonPokemon;

/** The main state for Dungeon exploration. */
public class DungeonState extends AbstractState {
    static class DefaultDungeonSubState extends DungeonSubState {
        public DefaultDungeonSubState(DungeonState parent) {
            super(parent);
        }

        @Override
        public void onKeyPressed(Key key) {
        }

        @Override
        public void onKeyReleased(Key key) {
        }

        @Override
        public void prerender(Graphics2D g, int width, int height) {
        }

        @Override
        public void render(Graphics2D g, int width, int height) {
        }

        @Override
        public void update() {
        }
    }

    /** A substate for Dungeon exploration. */
    static abstract class DungeonSubState extends AbstractState {
        public final DungeonState parent;

        public DungeonSubState(DungeonState parent) {
            this.parent = parent;
        }

        @Override
        public boolean isMain() {
            return this.parent.isMain() && this.parent.currentSubstate == this;
        }

        /**
         * First part of this State's rendering. Called while the camera translation is still active (=coordinates are
         * relative to the camera Pokemon).
         */
        public abstract void prerender(Graphics2D g, int width, int height);

    }

    public final ActionSelectionState actionSelectionState;
    private Point camera = new Point(0, 0);
    /** The Pokemon around which to draw. */
    private DungeonPokemon cameraPokemon;
    /** The current substate. */
    private DungeonSubState currentSubstate;
    public final DungeonSubState defaultSubstate = new DefaultDungeonSubState(this);
    boolean diagonal = false, rotating = false;
    public final FloorRenderer floorRenderer;
    public final FloorVisibility floorVisibility;
    public final GridRenderer gridRenderer;
    public final DungeonItemsRenderer itemRenderer;
    private Tile lastKnownCameraTile;
    public final DungeonLogger logger;
    public final DungeonPokemonRendererHolder pokemonRenderer;
    /** The last Camera. */
    private Point previousCamera;
    public final ShadowRenderer shadowRenderer;
    public final StaticAnimationsRenderer staticAnimationsRenderer;

    public DungeonState() {
        this.cameraPokemon = Persistence.player.getDungeonLeader();
        Persistence.dungeonRenderer = new MasterDungeonRenderer();
        this.floorRenderer = new FloorRenderer();
        this.gridRenderer = new GridRenderer();
        this.itemRenderer = new DungeonItemsRenderer();
        this.pokemonRenderer = new DungeonPokemonRendererHolder();
        for (DungeonPokemon pokemon : Persistence.floor.listPokemon())
            this.pokemonRenderer.register(pokemon);
        this.shadowRenderer = new ShadowRenderer();
        this.staticAnimationsRenderer = new StaticAnimationsRenderer();

        Persistence.dungeonRenderer.addRenderer(this.floorRenderer);
        Persistence.dungeonRenderer.addRenderer(this.gridRenderer);
        Persistence.dungeonRenderer.addRenderer(this.itemRenderer);
        Persistence.dungeonRenderer.addRenderer(this.shadowRenderer);
        Persistence.dungeonRenderer.addRenderer(this.staticAnimationsRenderer);

        this.logger = new DungeonLogger(this);
        this.currentSubstate = this.actionSelectionState = new ActionSelectionState(this);
        this.currentSubstate.onStart();
        this.floorVisibility = new FloorVisibility();
    }

    public Point camera() {
        return this.camera;
    }

    public DungeonPokemon getCameraPokemon() {
        return this.cameraPokemon;
    }

    @Override
    public void onEnd() {
        super.onEnd();
        this.logger.hideMessages();
        this.diagonal = false;
        this.rotating = false;
    }

    @Override
    public void onKeyPressed(Key key) {
        if (key == Key.DIAGONAL)
            this.diagonal = true;
        if (key == Key.ROTATE)
            this.rotating = true;

        this.currentSubstate.onKeyPressed(key);

    }

    @Override
    public void onKeyReleased(Key key) {
        if (key == Key.DIAGONAL)
            this.diagonal = false;
        if (key == Key.ROTATE)
            this.rotating = false;

        this.currentSubstate.onKeyReleased(key);
    }

    @Override
    public void onMouseClick(int x, int y) {
        super.onMouseClick(x, y);
        this.currentSubstate.onMouseClick(x, y);
    }

    @Override
    public void onMouseMove(int x, int y) {
        super.onMouseMove(x, y);
        this.currentSubstate.onMouseMove(x, y);
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        super.onMouseRightClick(x, y);
        this.currentSubstate.onMouseRightClick(x, y);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.floorVisibility.onCameraMoved();
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        DungeonPokemonRenderer r = Persistence.dungeonState.pokemonRenderer.getRenderer(this.cameraPokemon);
        int x = 0, y = 0;
        if (r == null) {
            if (this.lastKnownCameraTile == null) {
                x = Persistence.floor.getWidth() * TILE_SIZE / 2 - width / 2;
                y = Persistence.floor.getHeight() * TILE_SIZE / 2 - height / 2;
            } else {
                x = this.lastKnownCameraTile.x * TILE_SIZE + TILE_SIZE / 2 - width / 2;
                y = this.lastKnownCameraTile.y * TILE_SIZE + TILE_SIZE / 2 - height / 2;
            }
        } else {
            x = (int) (r.drawX() - width / 2);
            y = (int) (r.drawY() - height / 2);
        }

        if (Persistence.floor.data.hasCustomTileset()) {
            if (x + width > Persistence.floor.getWidth() * TILE_SIZE)
                x = Persistence.floor.getWidth() * TILE_SIZE - width;
            if (y + height > Persistence.floor.getHeight() * TILE_SIZE)
                y = Persistence.floor.getHeight() * TILE_SIZE - height;

            if (x < 0)
                x = 0;
            if (y < 0)
                y = 0;
        }
        this.camera = new Point(x, y);

        if (this.previousCamera == null || !this.previousCamera.equals(this.camera())) {
            this.floorRenderer.setXY(x, y);
            this.gridRenderer.setXY(x, y);
            this.itemRenderer.setXY(x, y);
            this.pokemonRenderer.setXY(x, y);
            this.shadowRenderer.setXY(x, y);
            this.staticAnimationsRenderer.setXY(x, y);
        }

        g.translate(-x, -y);
        Persistence.dungeonRenderer.render(g, width, height);
        this.currentSubstate.prerender(g, width, height);
        g.translate(x, y);
        this.currentSubstate.render(g, width, height);

        Color weather = Persistence.floor.currentWeather().weather.layer;
        if (weather != null) {
            g.setColor(weather);
            g.fillRect(0, 0, width, height);
        }

        if (this.isMain())
            this.logger.render(g, width, height);

        this.previousCamera = (Point) this.camera().clone();
    }

    public void setCamera(DungeonPokemon pokemon) {
        this.cameraPokemon = pokemon;
        this.floorVisibility.onCameraMoved();
    }

    public void setDefaultState() {
        this.setSubstate(this.defaultSubstate);
    }

    /** @param substate - The new substate to use. */
    public void setSubstate(DungeonSubState substate) {
        this.currentSubstate.onEnd();
        this.currentSubstate = substate;
        this.currentSubstate.onStart();
    }

    @Override
    public void update() {
        Persistence.dungeonRenderer.update();
        if (this.cameraPokemon != null && this.cameraPokemon.tile() != null)
            this.lastKnownCameraTile = this.cameraPokemon.tile();
        // this.pokemonRenderer.update(); Don't because the renderers are updated in MasterDungeonRenderer
        if (this.isMain())
            this.logger.update();
        this.currentSubstate.update();
    }

}

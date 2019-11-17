package com.darkxell.client.renderers;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Master Renderer for the DungeonState. All Renderers should be registered in this Master Renderer. It will call them
 * when needed.
 */
public class MasterDungeonRenderer {

    public static final int LAYER_TILES = 0, LAYER_GRID = 2, LAYER_ITEMS = 10, LAYER_POKEMON = 15, LAYER_SHADOWS = 25,
            LAYER_STATIC_ANIMATIONS = 29, LAYER_LOGGER = 30;

    private final ArrayList<AbstractRenderer> renderers = new ArrayList<>();
    private boolean updateRequired;

    public void addRenderer(AbstractRenderer renderer) {
        this.renderers.add(renderer);
        this.updateRequired = true;
    }

    public void clear() {
        this.renderers.clear();
    }

    public void onObjectUpdated() {
        this.updateRequired = true;
    }

    public void removeRenderer(AbstractRenderer renderer) {
        this.renderers.remove(renderer);
    }

    public void render(Graphics2D g, int width, int height) {
        if (this.updateRequired) {
            this.renderers.sort(Comparator.naturalOrder());
            this.updateRequired = false;
        }

        for (int i = 0; i < this.renderers.size(); ++i)
            if (this.renderers.get(i).shouldRender(width, height))
                this.renderers.get(i).render(g, width, height);
    }

    public void update() {
        for (int i = 0; i < this.renderers.size();++i)
            this.renderers.get(i).update();
    }

}

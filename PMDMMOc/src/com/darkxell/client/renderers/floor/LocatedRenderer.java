package com.darkxell.client.renderers.floor;

import com.darkxell.client.renderers.AbstractRenderer;

/**
 * A renderer that has locations to render objects at. Utility differs depending on the renderer.
 */
public abstract class LocatedRenderer extends AbstractRenderer {

    private double x, y;

    public LocatedRenderer(int x, int y, int z) {
        super(x, y, z);
    }

    public void setXLocation(double x) {
        this.x = x;
    }

    public void setXYLocation(double x, double y) {
        this.setXLocation(x);
        this.setYLocation(y);
    }

    public void setYLocation(double y) {
        this.y = y;
    }

    public double xLocation() {
        return this.x;
    }

    public double yLocation() {
        return this.y;
    }

}

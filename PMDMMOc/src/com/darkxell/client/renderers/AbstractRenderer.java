package com.darkxell.client.renderers;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;

public abstract class AbstractRenderer implements Comparable<AbstractRenderer> {

    private ArrayList<RenderOffset> appliedOffsets = new ArrayList<>();

    /**
     * Rendering locations. X is horizontal position, Y is vertical position, Z is drawing order. <br />
     * Objects with lowest Z will be drawn first. Then if Z is equal, lowest Y will be drawn, then lowest X.
     */
    private double x, y, z;

    public AbstractRenderer() {
        this(0, 0, 0);
    }

    public AbstractRenderer(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final double baseX() {
        return this.x;
    }

    public final double baseY() {
        return this.y;
    }

    public final double baseZ() {
        return this.z;
    }

    @Override
    public int compareTo(AbstractRenderer o) {
        if (this.z != o.z)
            return Double.compare(this.z, o.z);
        if (this.y != o.y)
            return Double.compare(this.y, o.y);
        return Double.compare(this.x, o.x);
    }

    public double drawX() {
        double x = this.baseX();
        for (RenderOffset offset : this.appliedOffsets)
            x += offset.xOffset();
        return x;
    }

    public double drawY() {
        double y = this.baseY();
        for (RenderOffset offset : this.appliedOffsets)
            y += offset.yOffset();
        return y;
    }

    public double drawZ() {
        return this.z;
    }

    private void onUpdate() {
        if (Persistence.dungeonState != null && Persistence.dungeonRenderer != null)
            Persistence.dungeonRenderer.onObjectUpdated();
    }

    public void registerOffset(RenderOffset offset) {
        if (!this.appliedOffsets.contains(offset))
            this.appliedOffsets.add(offset);
    }

    /**
     * Draws this Renderer's Object.
     *
     * @param g      - The Graphics context.
     * @param width  - Width of the screen.
     * @param height - Height of the screen.
     */
    public abstract void render(Graphics2D g, int width, int height);

    public void setX(double x) {
        this.x = x;
        this.onUpdate();
    }

    public void setXY(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

    public void setXYZ(double x, double y, double z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public void setY(double y) {
        this.y = y;
        this.onUpdate();
    }

    public void setZ(double z) {
        this.z = z;
        this.onUpdate();
    }

    /** @return True if this Renderer should be called. */
    public boolean shouldRender(int width, int height) {
        return true;
    }

    public void unregisterOffset(RenderOffset offset) {
        this.appliedOffsets.remove(offset);
    }

    public void update() {
    }

}

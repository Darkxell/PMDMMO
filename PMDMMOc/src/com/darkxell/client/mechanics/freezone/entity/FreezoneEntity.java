package com.darkxell.client.mechanics.freezone.entity;

import com.darkxell.client.graphics.renderer.DefaultFreezoneEntityRenderer;
import com.darkxell.client.graphics.AbstractRenderer;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.xml.XMLImmutableObject;
import com.darkxell.common.util.xml.XMLUtils;
import org.jdom2.Element;

import java.awt.*;

/**
 * Null behavior entity.
 *
 * <p>Should not ever be used as is on purpose, though nothing should break from using it like that.</p>
 *
 * <p>The player does not extend this class.</p>
 */
public class FreezoneEntity extends XMLImmutableObject {
    /**
     * The X position of the entity.
     */
    double posX;

    /**
     * the Y position of the entity.
     */
    double posY;

    /**
     * The width of the entity's hitbox.
     */
    double width;

    /**
     * The height of the entity's hitbox.
     */
    double height;

    /**
     * Is this entity solid?
     */
    boolean solid;

    /**
     * Is this entity interactive?
     */
    boolean interactive;

    {
        this.posX = -1;
        this.posY = -1;
        this.width = 1.8;
        this.height = 1.7;
        this.solid = false;
        this.interactive = false;
    }

    /**
     * Custom initialization behavior.
     *
     * <p>By default, it will attempt to extract the {@code x} and {@code y}, {@code solid} and {@code interactive}
     * properties from the attributes on the root element. A missing attribute will assume a default value.</p>
     *
     * <p>Subclasses will, obviously, always have the option to not parse these values and/or parse these values in a
     * different way, although this approach should be used sparingly and <em>must always be clearly documented</em>
     * to avoid unwelcome surprises to the designer.</p>
     */
    protected void deserialize(Element el) {
        this.posX = XMLUtils.getAttribute(el, "x", this.posX);
        this.posY = XMLUtils.getAttribute(el, "y", this.posY);

        this.solid = XMLUtils.getAttribute(el, "solid", this.solid);
        this.interactive = XMLUtils.getAttribute(el, "interactive", this.interactive);
    }

    /**
     * Called when the players interact with this entity.
     *
     * <p>Note that this might be called even if this entity has the {@code interactive} tag set to false. This method
     * will be called from the KeyEvent thread.</p>
     *
     * <p>The default implementation has no behavior.</p>
     */
    public void onInteract() {
    }

    /**
     * Prints this entity on the drawable graphics provided. graphics transition should have already been applied
     * before calling this method. This will use a 8px/unit position ratio.
     *
     * <p>This method needs to be implemented if this freezone entity has the default renderer. Alternatively,
     * supply your own renderer by overriding {@link #createRenderer()}.</p>
     *
     * <p>The default implementation has no behavior.</p>
     */
    public void print(Graphics2D g) {
    }

    /**
     * <p>The default implementation has no behavior.</p>
     */
    public void update() {
    }

    /**
     * Returns the hitbox of this entity at the wanted position.
     *
     * <p>To change the dimensions of the box, change {@link #width} and {@link #height}.</p>
     */
    public DoubleRectangle getHitbox(double x, double y) {
        return new DoubleRectangle(x, y, this.width, this.height, true);
    }

    public AbstractRenderer createRenderer() {
        return new DefaultFreezoneEntityRenderer(this);
    }

    public double getX() {
        return this.posX;
    }

    public double getY() {
        return this.posY;
    }

    public boolean isSolid() {
        return this.solid;
    }

    public boolean isInteractive() {
        return this.interactive;
    }
}

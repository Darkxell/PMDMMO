package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.mechanics.freezones.entities.renderers.DefaultFreezoneEntityRenderer;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.XMLObject;
import com.darkxell.common.util.XMLUtils;
import org.jdom2.Element;

import java.awt.*;

/**
 * Null behavior entity. Should not ever be used as is on purpose, though nothing should break from using it like that.
 *
 * <p>The player does not extend this class.</p>
 */
public class FreezoneEntity extends XMLObject {
    /**
     * The X position of the entity.
     */
    public double posX;

    /**
     * the Y position of the entity.
     */
    public double posY;

    /**
     * Is this entity solid?
     */
    protected boolean isSolid;

    /**
     * Is this entity interactive?
     */
    protected boolean canInteract;

    {
        this.posX = -1;
        this.posY = -1;
        this.isSolid = false;
        this.canInteract = false;
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
    protected void onInitialize(Element el) {
        this.posX = XMLUtils.getAttribute(el, "x", this.posX);
        this.posY = XMLUtils.getAttribute(el, "y", this.posY);

        this.isSolid = XMLUtils.getAttribute(el, "solid", this.isSolid);
        this.canInteract = XMLUtils.getAttribute(el, "interactive", this.canInteract);
    }

    /**
     * Called when the players interact with this entity.
     *
     * <p>Note that this might be called even if this entity has the {@code canInteract} tag set to false. This method
     * will be called from the KeyEvent thread.</p>
     */
    public void onInteract() {
    }

    /**
     * Prints this entity on the drawable graphics provided. graphics transition should have already been applied
     * before calling this method. This will use a 8px/unit position ratio.
     *
     * <p>This method needs to be implemented if this freezone entity has the default renderer. Alternatively,
     * supply your own renderer by overriding {@link #createRenderer()}.</p>
     */
    public void print(Graphics2D g) {
    }

    public void update() {
    }

    /**
     * Returns the hitbox of this entity at the wanted position. This returns the default hitbox size.
     */
    public DoubleRectangle getHitbox(double x, double y) {
        return new DoubleRectangle(x - 0.9, y - 0.9, 1.8, 1.7);
    }

    public AbstractRenderer createRenderer() {
        return new DefaultFreezoneEntityRenderer(this);
    }
}

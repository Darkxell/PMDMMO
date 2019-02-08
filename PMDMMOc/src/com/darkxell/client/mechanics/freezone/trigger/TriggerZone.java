package com.darkxell.client.mechanics.freezone.trigger;

import org.jdom2.Element;

import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.xml.XMLImmutableObject;
import com.darkxell.common.util.xml.XMLUtils;

/**
 * A zone on the map on which behavior can be applied once entered, e.g. warp zones, cutscene activation, or script
 * triggers.
 */
public abstract class TriggerZone extends XMLImmutableObject {
    private DoubleRectangle hitbox;

    @Override
    protected void deserialize(Element el) {
        if (this.hitbox == null)
            this.hitbox = new DoubleRectangle(0, 0, 0, 0);

        this.hitbox.x = XMLUtils.getAttribute(el, "x", this.hitbox.x);
        this.hitbox.y = XMLUtils.getAttribute(el, "y", this.hitbox.y);
        this.hitbox.width = XMLUtils.getAttribute(el, "width", this.hitbox.width);
        this.hitbox.height = XMLUtils.getAttribute(el, "height", this.hitbox.height);
    }

    public DoubleRectangle getHitbox() {
        return hitbox;
    }

    /**
     * Set other trigger zone as context.
     *
     * <p>
     * Currently only copies the hitbox.
     * </p>
     *
     * @param context What other zone to inherit properties from.
     */
    public void setContext(TriggerZone context) {
        if (this.initialized)
            throw new IllegalStateException("Cannot set trigger zone context after initialization");
        this.hitbox = context.getHitbox();
    }

    /**
     * Check if this trigger zone should be evaluated.
     *
     * <p>
     * All "top-level" trigger zones have this condition ignored. This may change in the future, if enough trigger zone
     * types are conditional.
     * </p>
     *
     * <p>
     * By default, this returns true and can be ignored in non-conditional trigger zones. This is currently only used by
     * {@link MultiTriggerZone} to implement fallback trigger zones./p>
     *
     * @return Is this trigger zone active?
     */
    public boolean isActive() {
        return true;
    }

    /**
     * Behavior upon entering the trigger zone.
     */
    public abstract void onEnter();
}

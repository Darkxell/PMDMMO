package com.darkxell.client.mechanics.freezones;

import com.darkxell.common.util.DoubleRectangle;

/**
 * A zone on the map on which behavior can be applied once entered, e.g. warp zones, cutscene activation, or script
 * triggers.
 */
public abstract class TriggerZone {
    public DoubleRectangle hitbox;

    /**
     * Creates a new trigger zone. A base trigger zone object has no set behavior.
     *
     * @param hitbox the hitbox of the trigger zone in the current map.
     */
    public TriggerZone(DoubleRectangle hitbox) {
        this.hitbox = hitbox;
    }

    /**
     * Behavior upon entering the trigger zone.
     */
    public abstract void onEnter();
}

package com.darkxell.client.mechanics.freezones.trigger;

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
     * Check if this trigger zone should be evaluated.
     *
     * <p>All "top-level" trigger zones have this condition ignored. This may change in the future, if enough
     * trigger zone types are conditional.</p>
     *
     * <p>By default, this returns true and can be ignored in non-conditional trigger zones. This is currently only
     * used by {@link MultiTriggerZone} to implement fallback trigger zones./p>
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

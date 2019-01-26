package com.darkxell.client.mechanics.freezones.trigger;

import com.darkxell.common.util.DoubleRectangle;

/**
 * Trigger zone that is able to skip past inactive trigger zones.
 *
 * <p>Child trigger zones will inherit their hitbox from the parent trigger zone. Therefore, if all trigger zones
 * have different hitboxes, you can simply leave those attributes off on the parent.</p>
 */
public class MultiTriggerZone extends TriggerZone {
    private TriggerZone[] childZones;

    /**
     * If a trigger is activated, should the other triggers be evaluated?
     */
    private boolean fallthrough;

    public MultiTriggerZone(DoubleRectangle hitbox, TriggerZone[] childZones, boolean fallthrough) {
        super(hitbox);

        this.childZones = childZones;
        this.fallthrough = fallthrough;
    }

    @Override
    public void onEnter() {
        for (TriggerZone zone : this.childZones) {
            if (zone.isActive()) {
                zone.onEnter();
                if (!this.fallthrough) {
                    return;
                }
            }
        }
    }
}

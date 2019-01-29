package com.darkxell.client.mechanics.freezone.trigger;

import com.darkxell.common.util.xml.XMLUtils;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void deserialize(Element el) {
        super.deserialize(el);

        this.fallthrough = XMLUtils.getAttribute(el, "fallthrough", false);
        this.childZones = this.getTriggerZones(el);
    }

    private TriggerZone[] getTriggerZones(Element el) {
        List<TriggerZone> childZones = new ArrayList<>();
        for (Element triggerEl : el.getChildren("trigger")) {
            childZones.add(TriggerZoneFactory.getZone(this, triggerEl));
        }

        return childZones.toArray(new TriggerZone[0]);
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

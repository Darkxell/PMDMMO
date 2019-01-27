package com.darkxell.client.mechanics.freezone.trigger;

import com.darkxell.client.state.StateManager;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.zones.FreezoneInfo;
import org.jdom2.Element;

/**
 * A zone that teleports a player to a position on any map.
 */
public class WarpZone extends TriggerZone {
    public int toX;
    public int toY;

    public FreezoneInfo destination;
    public Direction direction;

    @Override
    protected void deserialize(Element el) {
        super.deserialize(el);

        int direction = XMLUtils.getAttribute(el, "direction", 4);

        this.toX = XMLUtils.getAttribute(el, "destx", -1);
        this.toY = XMLUtils.getAttribute(el, "desty", -1);
        this.direction = Direction.DIRECTIONS.get(direction);
        this.destination = this.getDestination(el);
    }

    private FreezoneInfo getDestination(Element el) {
        String destination = el.getAttributeValue("dest");
        if (destination == null) {
            throw new IllegalArgumentException("There is no destination for this warp zone.");
        }
        return FreezoneInfo.find(destination);
    }

    @Override
    public void onEnter() {
        StateManager.setExploreState(this.destination, this.direction, this.toX, this.toY, true);
    }
}

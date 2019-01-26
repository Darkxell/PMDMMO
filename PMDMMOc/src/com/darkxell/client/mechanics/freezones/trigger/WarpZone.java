package com.darkxell.client.mechanics.freezones.trigger;

import com.darkxell.client.state.StateManager;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

/**
 * A zone that teleports a player to a position on any map.
 */
public class WarpZone extends TriggerZone {
    public int toX;
    public int toY;

    public FreezoneInfo destination;
    public Direction direction;

    public WarpZone(int x, int y, FreezoneInfo destination, DoubleRectangle hitbox) {
        this(x, y, destination, null, hitbox);
    }

    /**
     * A zone that teleports a player to a position on any map.
     *
     * @param x         Destination x position. (-1 replaced with default)
     * @param y         Destination y position. (-1 replaced with default)
     * @param hitbox    Hitbox of the warpzone in the current map.
     * @param direction New direction to face upon entering the new Freezone. (null for current direction)
     */
    public WarpZone(int x, int y, FreezoneInfo destination, Direction direction, DoubleRectangle hitbox) {
        super(hitbox);

        this.toX = x;
        this.toY = y;
        this.destination = destination;
        this.direction = direction;
    }

    @Override
    public void onEnter() {
        StateManager.setExploreState(this.destination, this.direction, this.toX, this.toY, true);
    }
}

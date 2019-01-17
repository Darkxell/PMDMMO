package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

public class FriendAreaFreezone extends FreezoneMap {
    public FriendAreaFreezone(int defaultX, int defaultY, FreezoneInfo info) {
        super("/freezones/friend/" + info.id + ".xml", defaultX, defaultY, info);

        int width = this.terrain.getWidth();
        int height = this.terrain.getHeight();
        this.triggerzones.add(new FreezoneMapTriggerZone(info, new DoubleRectangle(0, 0, width, 1)));
        this.triggerzones.add(new FreezoneMapTriggerZone(info, new DoubleRectangle(0, 0, 1, height)));
        this.triggerzones.add(new FreezoneMapTriggerZone(info, new DoubleRectangle(0, height, width, 1)));
        this.triggerzones.add(new FreezoneMapTriggerZone(info, new DoubleRectangle(width, 0, 1, height)));
    }
}

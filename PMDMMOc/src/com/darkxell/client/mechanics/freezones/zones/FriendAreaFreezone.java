package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

public class FriendAreaFreezone extends FreezoneMap
{

	public FriendAreaFreezone(int defaultX, int defaultY, FreezoneInfo info)
	{
		super("/freezones/" + info.id + ".xml", defaultX, defaultY, info);

		this.triggerzones.add(new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(0, 0, this.mapWidth, 1)));
		this.triggerzones.add(new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(0, 0, 1, this.mapHeight)));
		this.triggerzones.add(new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(0, this.mapHeight, this.mapWidth, 1)));
		this.triggerzones.add(new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(this.mapWidth, 0, 1, this.mapHeight)));
	}

}

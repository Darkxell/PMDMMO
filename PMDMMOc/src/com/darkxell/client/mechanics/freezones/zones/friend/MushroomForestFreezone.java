package com.darkxell.client.mechanics.freezones.zones.friend;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

public class MushroomForestFreezone extends FreezoneMap {

	public MushroomForestFreezone() {
		super("/freezones/mushroomforest.xml");
		// this.freezonebgm = "base.mp3";

		this.triggerzones.add(new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(14, 40, 28, 2)));
		this.triggerzones.add(new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(0, 29, 2, 4)));
		this.triggerzones.add(new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(55, 29, 2, 4)));
		this.triggerzones.add(new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(12, 0, 3, 2)));
		this.triggerzones.add(new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(42, 0, 3, 2)));
	}

	@Override
	public FreezoneInfo getInfo() {
		return FreezoneInfo.FRIEND_MUSHROOMFOREST;
	}

	@Override
	public int defaultX() {
		return 28;
	}

	@Override
	public int defaultY() {
		return 38;
	}

}

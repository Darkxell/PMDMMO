package com.darkxell.client.mechanics.freezones.zones.friend;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

public class MtMoonviewFreezone extends FreezoneMap {

	public MtMoonviewFreezone() {
		super("/freezones/mtmoonview.xml");
		// this.freezonebgm = "base.mp3";

		this.triggerzones.add(new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(24, 40, 11, 2)));
		this.triggerzones.add(new FreezoneMapTriggerZone(this.getInfo(), new DoubleRectangle(0, 12, 2, 4)));
	}

	@Override
	public FreezoneInfo getInfo() {
		return FreezoneInfo.FRIEND_MTMOONVIEW;
	}

	@Override
	public int defaultX() {
		return 30;
	}

	@Override
	public int defaultY() {
		return 38;
	}

}

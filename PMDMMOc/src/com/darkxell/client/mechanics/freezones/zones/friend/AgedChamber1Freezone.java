package com.darkxell.client.mechanics.freezones.zones.friend;

import com.darkxell.client.mechanics.freezones.FreezoneInfo;
import com.darkxell.client.mechanics.freezones.FreezoneMap;

public class AgedChamber1Freezone extends FreezoneMap {

	public AgedChamber1Freezone() {
		super("/freezones/agedchamber1.xml");
		// this.freezonebgm = "base.mp3";
	}

	@Override
	public FreezoneInfo getInfo() {
		return FreezoneInfo.FRIEND_AGEDCHAMBER1;
	}

	@Override
	public int defaultX() {
		return 29;
	}

	@Override
	public int defaultY() {
		return 35;
	}

}

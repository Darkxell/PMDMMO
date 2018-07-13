package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneInfo;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.common.util.DoubleRectangle;

public class ForestFreezone extends FreezoneMap {

	public ForestFreezone() {
		super("/freezones/forest.xml");
		this.freezonebgm = "base.mp3";
		this.triggerzones.add(new WarpZone(6, 41, FreezoneInfo.BASE, new DoubleRectangle(62, 17, 2, 10)));
	}

	@Override
	public FreezoneInfo getInfo() {
		return FreezoneInfo.STARTFOREST;
	}

	@Override
	public int defaultX() {
		return 35;
	}

	@Override
	public int defaultY() {
		return 22;
	}

}

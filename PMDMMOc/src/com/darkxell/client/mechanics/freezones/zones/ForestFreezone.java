package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

public class ForestFreezone extends FreezoneMap {

	public ForestFreezone() {
		super(readModel("/freezones/forest.xml"), 35, 22, FreezoneInfo.STARTFOREST);
		this.freezonebgm = "base.mp3";
		this.triggerzones.add(new WarpZone(6, 41, FreezoneInfo.BASE, new DoubleRectangle(62, 17, 2, 10)));
	}

}

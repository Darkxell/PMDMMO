package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

public class BaseInsideFreezone extends FreezoneMap{

	public BaseInsideFreezone() {
		super("/freezones/base_normal.xml", 23, 13, FreezoneInfo.BASEINSIDE);
		this.freezonebgm = "base.mp3";
		this.triggerzones.add(new WarpZone(35, 28, FreezoneInfo.BASE, new DoubleRectangle(20,32,5,2)));
	}

}

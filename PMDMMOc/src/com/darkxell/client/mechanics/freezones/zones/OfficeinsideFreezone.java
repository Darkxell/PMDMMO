package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

public class OfficeinsideFreezone extends FreezoneMap {

	public OfficeinsideFreezone() {
		super("/freezones/officeinside.xml", 23, 34, FreezoneInfo.OFFICEINSIDE);
		this.freezonebgm = "town.mp3";
		this.triggerzones.add(new WarpZone(49, 25, FreezoneInfo.OFFICE, new DoubleRectangle(21, 36, 5, 2)));
	}

}

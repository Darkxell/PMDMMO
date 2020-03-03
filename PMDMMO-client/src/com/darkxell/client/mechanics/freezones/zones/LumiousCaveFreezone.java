package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.CrystalEntity;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

public class LumiousCaveFreezone extends FreezoneMap {

	public LumiousCaveFreezone() {
		super(readModel("/freezones/lcave.xml"), 29, 34, FreezoneInfo.LUMINOUSCAVE);
		this.freezonebgm = "pond.mp3";
		this.triggerzones.add(new WarpZone(29, 15, FreezoneInfo.POND, new DoubleRectangle(28, 38, 4, 2)));

		this.addEntity(new CrystalEntity(30.3, 26.3));
	}

}

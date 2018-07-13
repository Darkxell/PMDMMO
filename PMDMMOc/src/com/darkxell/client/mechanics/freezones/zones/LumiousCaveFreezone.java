package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneInfo;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.CrystalEntity;
import com.darkxell.common.util.DoubleRectangle;

public class LumiousCaveFreezone extends FreezoneMap {

	public LumiousCaveFreezone() {
		super("/freezones/lcave.xml");
		this.freezonebgm = "pond.mp3";
		this.triggerzones.add(new WarpZone(29, 15, FreezoneInfo.POND, new DoubleRectangle(28, 38, 4, 2)));

		this.addEntity(new CrystalEntity(30.3, 26.3));
	}

	@Override
	public FreezoneInfo getInfo() {
		return FreezoneInfo.LUMINOUSCAVE;
	}

	@Override
	public int defaultX() {
		return 29;
	}

	@Override
	public int defaultY() {
		return 34;
	}

}

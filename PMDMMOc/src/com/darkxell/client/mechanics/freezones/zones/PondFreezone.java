package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.AnimatedFlowerEntity;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

public class PondFreezone extends FreezoneMap {

	public PondFreezone() {
		super("/freezones/pond.xml", 29, 60, FreezoneInfo.POND);
		this.freezonebgm = "pond.mp3";
		this.triggerzones.add(new WarpZone(64, 4, FreezoneInfo.SQUARE, new DoubleRectangle(25, 62, 7, 2)));
		this.triggerzones.add(new WarpZone(29, 34, FreezoneInfo.LUMINOUSCAVE, new DoubleRectangle(10, 18, 3, 3)));
		this.addEntity(new AnimatedFlowerEntity(14, 25, false));
		this.addEntity(new AnimatedFlowerEntity(43, 25, true));
		this.addEntity(new AnimatedFlowerEntity(17, 35, true));
		this.addEntity(new AnimatedFlowerEntity(40, 36, false));
	}

}
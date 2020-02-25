package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.AnimatedFlowerEntity;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

public class DojoFreezone extends FreezoneMap {

	public DojoFreezone() {
		super(readModel("/freezones/dojo.xml"), 42, 4, FreezoneInfo.DOJO);
		this.freezonebgm = "town.mp3";
		this.triggerzones.add(new WarpZone(67, 85, FreezoneInfo.SQUARE, new DoubleRectangle(39, 0, 9, 2)));

		this.addEntity(new AnimatedFlowerEntity(5, 6, false));
		this.addEntity(new AnimatedFlowerEntity(5, 22, false));
		this.addEntity(new AnimatedFlowerEntity(5, 38, false));
		this.addEntity(new AnimatedFlowerEntity(31, 45, false));
		this.addEntity(new AnimatedFlowerEntity(36, 21, false));
		this.addEntity(new AnimatedFlowerEntity(34, 15, false));
		this.addEntity(new AnimatedFlowerEntity(47, 23, false));
		this.addEntity(new AnimatedFlowerEntity(56, 29, false));
		this.addEntity(new AnimatedFlowerEntity(63, 29, false));
		this.addEntity(new AnimatedFlowerEntity(34, 12, true));
		this.addEntity(new AnimatedFlowerEntity(9, 46, true));
		this.addEntity(new AnimatedFlowerEntity(13, 44, true));
		this.addEntity(new AnimatedFlowerEntity(26, 45, true));
		this.addEntity(new AnimatedFlowerEntity(48, 26, true));
		this.addEntity(new AnimatedFlowerEntity(58, 26, true));
		this.addEntity(new AnimatedFlowerEntity(62, 32, true));
		this.addEntity(new AnimatedFlowerEntity(73, 39, true));
		this.addEntity(new AnimatedFlowerEntity(68, 40, true));

	}

}

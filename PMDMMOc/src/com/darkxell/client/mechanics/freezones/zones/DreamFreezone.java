package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.renderers.layers.WetDreamLayer;
import com.darkxell.common.zones.FreezoneInfo;

public class DreamFreezone extends FreezoneMap {

	public WetDreamLayer background = new WetDreamLayer();

	public DreamFreezone() {
		super("/freezones/void.xml", 15, 27, FreezoneInfo.DREAM);
		this.freezonebgm = "base.mp3";
	}

}

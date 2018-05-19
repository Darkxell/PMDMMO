package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.state.map.LocalMap.LOCALMAPLOCATION;
import com.darkxell.common.util.DoubleRectangle;

public class ForestFreezone extends FreezoneMap {

	public ForestFreezone() {
		super("/freezones/forest.xml");
		this.freezonebgm = "10 Rescue Team Base.mp3";
		this.warpzones.add(new WarpZone(6, 41, new DoubleRectangle(62, 17, 2, 10)) {
			@Override
			public FreezoneMap getDestination() {
				return new BaseFreezone();
			}
		});
	}

	@Override
	public LOCALMAPLOCATION getMapLocation() {
		return LOCALMAPLOCATION.STRATFOREST;
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

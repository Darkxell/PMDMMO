package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.common.util.DoubleRectangle;

public class BaseFreezone extends FreezoneMap {

	public BaseFreezone() {
		super("resources\\freezones\\base.xml");
		this.freezonebgm = "10 Rescue Team Base.mp3";
		this.warpzones.add(new WarpZone(4, 40, new DoubleRectangle(66, 34, 2, 11)) {
			@Override
			public FreezoneMap getDestination() {
				return new PokemonSquareFreezone();
			}
		});
		
	}

}

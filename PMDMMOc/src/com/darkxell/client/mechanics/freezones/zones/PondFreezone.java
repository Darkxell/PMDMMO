package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.common.util.DoubleRectangle;

public class PondFreezone extends FreezoneMap{

	public PondFreezone() {
		super("resources\\freezones\\pond.xml");
		this.freezonebgm = "14 Pokemon Square.mp3";
		this.warpzones.add(new WarpZone(64, 4, new DoubleRectangle(25, 62, 7, 2)) {
			@Override
			public FreezoneMap getDestination() {
				return new PokemonSquareFreezone();
			}
		});
	}

}
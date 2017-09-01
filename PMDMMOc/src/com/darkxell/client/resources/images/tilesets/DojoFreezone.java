package com.darkxell.client.resources.images.tilesets;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.zones.PokemonSquareFreezone;
import com.darkxell.common.util.DoubleRectangle;

public class DojoFreezone extends FreezoneMap {

	public DojoFreezone() {
		super("resources\\freezones\\dojo.xml");
		this.freezonebgm = "14 Pokemon Square.mp3";
		this.warpzones.add(new WarpZone(67, 85, new DoubleRectangle(39, 0, 9, 2)) {
			@Override
			public FreezoneMap getDestination() {
				return new PokemonSquareFreezone();
			}
		});
	}

}

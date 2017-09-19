package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.renderers.layers.BackgroundSeaLayer;
import com.darkxell.common.util.DoubleRectangle;

public class OfficeFreezone extends FreezoneMap {

	public BackgroundSeaLayer background = new BackgroundSeaLayer();

	public OfficeFreezone() {
		super("resources\\freezones\\office.xml");
		this.freezonebgm = "14 Pokemon Square.mp3";
		this.warpzones.add(new WarpZone(116, 40, new DoubleRectangle(0, 25, 2, 9)) {
			@Override
			public FreezoneMap getDestination() {
				return new PokemonSquareFreezone();
			}
		});
	}

}

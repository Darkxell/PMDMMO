package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.AnimatedFlowerEntity;
import com.darkxell.common.util.DoubleRectangle;

public class PondFreezone extends FreezoneMap {

	public PondFreezone() {
		super("resources\\freezones\\pond.xml");
		this.freezonebgm = "14 Pokemon Square.mp3";
		this.warpzones.add(new WarpZone(64, 4, new DoubleRectangle(25, 62, 7, 2)) {
			@Override
			public FreezoneMap getDestination() {
				return new PokemonSquareFreezone();
			}
		});
		this.entities.add(new AnimatedFlowerEntity(14, 25, false));
		this.entities.add(new AnimatedFlowerEntity(43, 25, true));
		this.entities.add(new AnimatedFlowerEntity(17, 35, true));
		this.entities.add(new AnimatedFlowerEntity(40, 36, false));
	}

}
package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.AnimatedFlowerEntity;
import com.darkxell.client.state.map.LocalMap.LOCALMAPLOCATION;
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

		this.entities.add(new AnimatedFlowerEntity(5, 6, false));
		this.entities.add(new AnimatedFlowerEntity(5, 22, false));
		this.entities.add(new AnimatedFlowerEntity(5, 38, false));
		this.entities.add(new AnimatedFlowerEntity(31, 45, false));
		this.entities.add(new AnimatedFlowerEntity(36, 21, false));
		this.entities.add(new AnimatedFlowerEntity(34, 15, false));
		this.entities.add(new AnimatedFlowerEntity(47, 23, false));
		this.entities.add(new AnimatedFlowerEntity(56, 29, false));
		this.entities.add(new AnimatedFlowerEntity(63, 29, false));

	}

	@Override
	public LOCALMAPLOCATION getMapLocation() {
		return LOCALMAPLOCATION.DOJO;
	}

}

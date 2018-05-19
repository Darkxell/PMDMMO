package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.AnimatedFlowerEntity;
import com.darkxell.client.state.map.LocalMap.LOCALMAPLOCATION;
import com.darkxell.common.util.DoubleRectangle;

public class DojoFreezone extends FreezoneMap {

	public DojoFreezone() {
		super("/freezones/dojo.xml");
		this.freezonebgm = "14 Pokemon Square.mp3";
		this.warpzones.add(new WarpZone(67, 85, new DoubleRectangle(39, 0, 9, 2)) {
			@Override
			public FreezoneMap getDestination() {
				return new PokemonSquareFreezone();
			}
		});

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

	@Override
	public LOCALMAPLOCATION getMapLocation() {
		return LOCALMAPLOCATION.DOJO;
	}

	@Override
	public int defaultX()
	{
		return 42;
	}

	@Override
	public int defaultY()
	{
		return 4;
	}

}

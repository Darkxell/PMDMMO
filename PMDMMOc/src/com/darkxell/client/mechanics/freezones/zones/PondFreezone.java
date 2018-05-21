package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.AnimatedFlowerEntity;
import com.darkxell.client.state.map.LocalMap.LOCALMAPLOCATION;
import com.darkxell.common.util.DoubleRectangle;

public class PondFreezone extends FreezoneMap {

	public PondFreezone() {
		super("/freezones/pond.xml");
		this.freezonebgm = "pond.mp3";
		this.warpzones.add(new WarpZone(64, 4, new DoubleRectangle(25, 62, 7, 2)) {
			@Override
			public FreezoneMap getDestination() {
				return new PokemonSquareFreezone();
			}
		});
		this.warpzones.add(new WarpZone(29, 34, new DoubleRectangle(10, 18, 3, 3)) {
			@Override
			public FreezoneMap getDestination() {
				return new LumiousCaveFreezone();
			}
		});
		this.addEntity(new AnimatedFlowerEntity(14, 25, false));
		this.addEntity(new AnimatedFlowerEntity(43, 25, true));
		this.addEntity(new AnimatedFlowerEntity(17, 35, true));
		this.addEntity(new AnimatedFlowerEntity(40, 36, false));
	}

	@Override
	public LOCALMAPLOCATION getMapLocation() {
		return LOCALMAPLOCATION.POND;
	}

	@Override
	public int defaultX()
	{
		return 29;
	}

	@Override
	public int defaultY()
	{
		return 60;
	}

}
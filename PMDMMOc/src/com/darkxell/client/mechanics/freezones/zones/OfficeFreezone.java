package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.AnimatedFlowerEntity;
import com.darkxell.client.renderers.layers.BackgroundSeaLayer;
import com.darkxell.client.state.map.LocalMap.LOCALMAPLOCATION;
import com.darkxell.common.util.DoubleRectangle;

public class OfficeFreezone extends FreezoneMap {

	public BackgroundSeaLayer background = new BackgroundSeaLayer();

	public OfficeFreezone() {
		super("/freezones/office.xml");
		this.freezonebgm = "town.mp3";
		this.warpzones.add(new WarpZone(116, 40, new DoubleRectangle(0, 25, 2, 9)) {
			@Override
			public FreezoneMap getDestination() {
				return new PokemonSquareFreezone();
			}
		});

		this.addEntity(new AnimatedFlowerEntity(7, 25, false));
		this.addEntity(new AnimatedFlowerEntity(4, 47, false));
		this.addEntity(new AnimatedFlowerEntity(20, 41, false));
		this.addEntity(new AnimatedFlowerEntity(43, 26, false));

		this.addEntity(new AnimatedFlowerEntity(18, 21, true));
		this.addEntity(new AnimatedFlowerEntity(23, 20, true));
		this.addEntity(new AnimatedFlowerEntity(55, 24, true));
		this.addEntity(new AnimatedFlowerEntity(40, 39, true));
		this.addEntity(new AnimatedFlowerEntity(15, 46, true));
	}

	@Override
	public LOCALMAPLOCATION getMapLocation() {
		return LOCALMAPLOCATION.OFFICE;
	}

	@Override
	public int defaultX()
	{
		return 4;
	}

	@Override
	public int defaultY()
	{
		return 30;
	}

}

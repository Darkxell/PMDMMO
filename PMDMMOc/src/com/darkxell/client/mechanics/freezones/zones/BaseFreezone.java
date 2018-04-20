package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.AnimatedFlowerEntity;
import com.darkxell.client.mechanics.freezones.entities.FlagEntity;
import com.darkxell.client.state.map.LocalMap.LOCALMAPLOCATION;
import com.darkxell.client.state.menu.freezone.DungeonSelectionMapState;
import com.darkxell.common.util.DoubleRectangle;

public class BaseFreezone extends FreezoneMap {

	public BaseFreezone() {
		super("/freezones/base.xml");
		this.freezonebgm = "10 Rescue Team Base.mp3";
		this.warpzones.add(new WarpZone(4, 40, new DoubleRectangle(66, 34, 2, 11)) {
			@Override
			public FreezoneMap getDestination() {
				return new PokemonSquareFreezone();
			}
		});
		final BaseFreezone f = this;
		this.warpzones.add(new WarpZone(35, 29, new DoubleRectangle(29, 63, 9, 2)) {
			@Override
			public FreezoneMap getDestination() {
				Persistance.stateManager.setState(new DungeonSelectionMapState());
				return f;
			}
		});
		this.warpzones.add(new WarpZone(0, 0, new DoubleRectangle(0, 34, 2,11)) {
			@Override
			public FreezoneMap getDestination() {
				CutsceneManager.playCutsene("test");
				return null;
			}
		});
		this.addEntity(new AnimatedFlowerEntity(17.5, 16, true));
		this.addEntity(new AnimatedFlowerEntity(33.5, 8, true));
		this.addEntity(new AnimatedFlowerEntity(54.5, 18, true));
		this.addEntity(new AnimatedFlowerEntity(61.5, 31, true));
		this.addEntity(new AnimatedFlowerEntity(54.5, 48, true));
		this.addEntity(new AnimatedFlowerEntity(4.5, 37, false));
		this.addEntity(new AnimatedFlowerEntity(16.5, 20, false));
		this.addEntity(new AnimatedFlowerEntity(25.5, 8, false));
		this.addEntity(new AnimatedFlowerEntity(41.5, 8, false));
		this.addEntity(new AnimatedFlowerEntity(59.5, 27, false));
		this.addEntity(new AnimatedFlowerEntity(56.5, 32, false));
		this.addEntity(new AnimatedFlowerEntity(42.5, 59, false));

		this.addEntity(new FlagEntity(24.4, 10));
	}

	@Override
	public LOCALMAPLOCATION getMapLocation() {
		return LOCALMAPLOCATION.BASE;
	}

}

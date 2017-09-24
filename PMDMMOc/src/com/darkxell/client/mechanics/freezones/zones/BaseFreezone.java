package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.AnimatedFlowerEntity;
import com.darkxell.client.mechanics.freezones.entities.FlagEntity;
import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.map.LocalMap.LOCALMAPLOCATION;
import com.darkxell.common.dungeon.DungeonRegistry;
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
		this.warpzones.add(new WarpZone(35, 29, new DoubleRectangle(29, 63, 9, 2)) {
			@Override
			public FreezoneMap getDestination() {
				DungeonPersistance.dungeon = DungeonRegistry.find(1).newInstance();
				DungeonPersistance.floor = DungeonPersistance.dungeon.currentFloor();
				DungeonPersistance.floor.generate();
				Launcher.stateManager.setState(DungeonPersistance.dungeonState = new DungeonState());
				return new BaseFreezone();
			}
		});
		this.entities.add(new AnimatedFlowerEntity(17.5, 16, true));
		this.entities.add(new AnimatedFlowerEntity(33.5, 8, true));
		this.entities.add(new AnimatedFlowerEntity(54.5, 18, true));
		this.entities.add(new AnimatedFlowerEntity(61.5, 31, true));
		this.entities.add(new AnimatedFlowerEntity(54.5, 48, true));
		this.entities.add(new AnimatedFlowerEntity(4.5, 37, false));
		this.entities.add(new AnimatedFlowerEntity(16.5, 20, false));
		this.entities.add(new AnimatedFlowerEntity(25.5, 8, false));
		this.entities.add(new AnimatedFlowerEntity(41.5, 8, false));
		this.entities.add(new AnimatedFlowerEntity(59.5, 27, false));
		this.entities.add(new AnimatedFlowerEntity(56.5, 32, false));
		this.entities.add(new AnimatedFlowerEntity(42.5, 59, false));

		this.entities.add(new FlagEntity(24.4, 10));
	}

	@Override
	public LOCALMAPLOCATION getMapLocation() {
		return LOCALMAPLOCATION.BASE;
	}

}

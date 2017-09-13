package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.common.dungeon.DungeonRegistry;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.layout.Layout;
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
				Floor f = new Floor(4, Layout.STATIC, DungeonRegistry.find(1));
				f.generate();
				Launcher.stateManager.setState(new DungeonState(f));
				return new BaseFreezone();
			}
		});

	}

}

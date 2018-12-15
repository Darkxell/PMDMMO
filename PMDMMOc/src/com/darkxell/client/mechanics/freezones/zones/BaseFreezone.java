package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.TriggerZone;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.AnimatedFlowerEntity;
import com.darkxell.client.mechanics.freezones.entities.Debugentity;
import com.darkxell.client.mechanics.freezones.entities.FlagEntity;
import com.darkxell.client.state.menu.freezone.DungeonSelectionMapState;
import com.darkxell.client.state.menu.freezone.FriendSelectionState;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

public class BaseFreezone extends FreezoneMap {

	public BaseFreezone() {
		super("/freezones/base.xml", 35, 28, FreezoneInfo.BASE);
		this.freezonebgm = "base.mp3";
		this.triggerzones.add(new WarpZone(4, 40, FreezoneInfo.SQUARE, new DoubleRectangle(66, 34, 2, 11)));
		this.triggerzones.add(new WarpZone(22, 30, FreezoneInfo.BASEINSIDE, new DoubleRectangle(32, 20, 3, 2)));
		this.triggerzones.add(new TriggerZone(new DoubleRectangle(0, 38, 2, 8)) {
			@Override
			public void onEnter() {
				Persistance.stateManager.setState(new FriendSelectionState(Persistance.stateManager.getCurrentState()));
			}
		});
		this.triggerzones.add(new TriggerZone(new DoubleRectangle(29, 63, 9, 2)) {
			@Override
			public void onEnter() {
				Persistance.stateManager.setState(new DungeonSelectionMapState());
			}
		});
		/*
		 * this.triggerzones.add(new WarpZone(0, 0, new DoubleRectangle(0, 38,
		 * 2, 8)) {
		 * 
		 * @Override public FreezoneMap getDestination() {
		 * CutsceneManager.playCutscene("test"); return null; } });
		 */
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

		this.addEntity(new Debugentity(50, 34));

		this.triggerzones.add(new TriggerZone(new DoubleRectangle(32, 25, 4, 4)) {
			@Override
			public void onEnter() {
				if (Persistance.player.getData().storyposition == 4) {
					CutsceneManager.playCutscene("base/magnetialert", false);
					Persistance.player.setStoryPosition(5);
					// Note that at this point, the client is offsync with the
					// server, as the server storypos is 4. both will be 5 at
					// the end of the cutscene.
				} else if(Persistance.player.getData().storyposition == 8) {
					CutsceneManager.playCutscene("base/squareopening", false);
					Persistance.player.setStoryPosition(10);
				}
			}
		});
	}

}

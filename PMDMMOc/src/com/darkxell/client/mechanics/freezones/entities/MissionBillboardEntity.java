package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.state.menu.freezone.MissionBoardSelectionState;

public class MissionBillboardEntity extends FreezoneEntity {

	public MissionBillboardEntity(double x, double y) {
		super(false, true, x, y);
		// technically not solid, shouldn't matter.
	}

	@Override
	public void onInteract() {
		Persistence.stateManager.setState(new MissionBoardSelectionState(Persistence.stateManager.getCurrentState()));
	}

	@Override
	public void update() {
	}

}

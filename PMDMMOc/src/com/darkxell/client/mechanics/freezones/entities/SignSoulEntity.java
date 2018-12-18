package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.common.util.language.Message;

/**
 * Describes a sign. Note that this entity doesn't have a graphical display and
 * can be walked through. On interact, it will open a simple dialogstate.
 */
public class SignSoulEntity extends FreezoneEntity {

	private Message mess;

	public SignSoulEntity(double x, double y, Message m) {
		super(false, true, x, y);
		this.mess = m;
	}

	@Override
	public void onInteract() {
		Persistence.stateManager.setState(new DialogState(Persistence.stateManager.getCurrentState(),
				new DialogScreen(this.mess).setInstant().setCentered()));
	}

	@Override
	public void update() {
	}

}

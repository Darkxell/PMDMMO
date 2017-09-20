package com.darkxell.client.mechanics.freezones.entities;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.state.DialogState;
import com.darkxell.client.state.DialogState.DialogElement;
import com.darkxell.common.util.Message;

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
		Launcher.stateManager
				.setState(new DialogState(Launcher.stateManager.getCurrentState(), new DialogElement(null, mess)));
	}

	@Override
	public void print(Graphics2D g) {
	}

	@Override
	public void update() {
	}

}

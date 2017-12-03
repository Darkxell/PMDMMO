package com.darkxell.client.mechanics.freezones.entities;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.state.DialogState;
import com.darkxell.client.state.DialogState.DialogScreen;
import com.darkxell.client.state.mainstates.PrincipalMainState;
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
		ArrayList<DialogScreen> screens = new ArrayList<DialogState.DialogScreen>();
		screens.add(new DialogScreen(null, this.mess).setInstant().setCentered());
		if(Persistance.stateManager instanceof PrincipalMainState)
			((PrincipalMainState) Persistance.stateManager).setState(new DialogState(((PrincipalMainState) Persistance.stateManager).getCurrentState(), screens));
	}

	@Override
	public void print(Graphics2D g) {
	}

	@Override
	public void update() {
	}

}

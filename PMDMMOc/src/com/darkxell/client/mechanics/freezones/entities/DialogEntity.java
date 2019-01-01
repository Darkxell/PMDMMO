package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;

public class DialogEntity extends FreezoneEntity {

	private DialogScreen[] dialogs;

	public DialogEntity(boolean isSolid, double x, double y) {
		this(isSolid, x, y, null);
	}

	public DialogEntity(boolean isSolid, double x, double y, DialogScreen[] dialogs) {
		super(isSolid, dialogs != null, x, y);
		this.dialogs = dialogs;
	}

	@Override
	public void onInteract() {
		if (this.dialogs != null) Persistence.stateManager.setState(
				new DialogState(Persistence.stateManager.getCurrentState(), null, this.dialogs).setOpaque(true));
	}

	@Override
	public void update() {}

}

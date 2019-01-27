package com.darkxell.client.state.menu.freezone;

import java.awt.Rectangle;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.graphics.AbstractGraphicsLayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class MyMissionsChoiceState extends OptionSelectionMenuState {

	public MyMissionsChoiceState(AbstractGraphicsLayer backgroundState, Mission mission) {
		super(backgroundState, true);
		this.createOptions();
		this.missioncontent = mission;
	}

	private Mission missioncontent;

	private MenuOption check;
	private MenuOption delete;
	private MenuOption exit;

	@Override
	protected void createOptions() {
		MenuTab tab = new MenuTab();
		tab.addOption(this.check = new MenuOption("mission.job.check"));
		tab.addOption(this.delete = new MenuOption("mission.job.delete"));
		tab.addOption(this.exit = new MenuOption("general.back"));
		this.tabs.add(tab);
	}

	@Override
	protected void onExit() {
		Persistence.stateManager.setState((AbstractState) this.background);
	}

	@Override
	protected void onOptionSelected(MenuOption option) {
		if (option == check) {
			Persistence.stateManager
					.setState(new MissionDetailsState((AbstractState) this.background, this.missioncontent, this));
		} else if (option == delete) {
			Logger.i("Deleting mission : " + missioncontent);
			JsonObject message = Json.object();
			message.add("action", "deletemission");
			message.add("mission", missioncontent.toString());
			Persistence.socketendpoint.sendMessage(message.toString());
			if (Persistence.player.getMissions().remove(missioncontent.toString())) {
				if (this.background instanceof MyMissionsState)
					((MyMissionsState) this.background).refresh();
				this.onExit();
			} else
				Logger.e("Could not delete mission : " + missioncontent);
		} else if (option == exit)
			this.onExit();
	}

	@Override
	protected Rectangle mainWindowDimensions() {
		Rectangle r = super.mainWindowDimensions();
		r.x = PrincipalMainState.displayWidth - r.width - r.x;
		r.y *= 2;
		return r;
	}

}

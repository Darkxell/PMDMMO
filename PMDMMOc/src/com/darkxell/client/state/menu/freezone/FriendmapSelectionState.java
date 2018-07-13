package com.darkxell.client.state.menu.freezone;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.freezones.FreezoneInfo;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.util.language.Message;

public class FriendmapSelectionState extends OptionSelectionMenuState {

	public static class MenuZoneOption extends MenuOption {
		public final FreezoneInfo info;

		public MenuZoneOption(FreezoneInfo info) {
			super(info.getName());
			this.info = info;
		}
	}

	private FriendAreaSelectionMapState parent;
	private MenuOption exit;
	private ArrayList<FreezoneInfo> destinations;

	public FriendmapSelectionState(FriendAreaSelectionMapState parent, ArrayList<FreezoneInfo> destinations) {
		super(parent, false);
		this.parent = parent;
		this.destinations = destinations;
		this.createOptions();
	}

	@Override
	protected void createOptions() {
		MenuTab tab = new MenuTab(this.destinations.isEmpty() ? new Message("", false) : this.destinations.get(0).maplocation.displayname);
		for (int i = 0; i < destinations.size(); i++)
			tab.addOption(new MenuZoneOption(destinations.get(i)));
		tab.addOption(this.exit = new MenuOption("general.back"));
		this.tabs.add(tab);
	}

	@Override
	protected void onExit() {
		Persistance.stateManager.setState(this.parent);
	}

	@Override
	protected void onOptionSelected(MenuOption option) {
		if (option == exit)
			this.onExit();
		else if (option instanceof MenuZoneOption) {
			// TODO
		}
	}

	@Override
	protected Rectangle mainWindowDimensions() {
		Rectangle r = super.mainWindowDimensions();
		return r;
	}

}

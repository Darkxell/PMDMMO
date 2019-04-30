package com.darkxell.client.state.menu.freezone;

import java.awt.Rectangle;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.menus.MovesMenuState;
import com.darkxell.client.state.menu.menus.TeamMenuState;
import com.darkxell.common.pokemon.Pokemon;

public class TeamMemberActionSelectionState extends OptionSelectionMenuState {

	public final TeamMenuState parent;
	public final Pokemon pokemon;
	private MenuOption summary, moves, leave, back;
	public final FreezoneMenuState superParent;

	public TeamMemberActionSelectionState(FreezoneMenuState superParent, TeamMenuState parent, Pokemon pokemon) {
		super(parent);
		this.parent = parent;
		this.superParent = superParent;
		this.pokemon = pokemon;
		this.createOptions();
	}

	@Override
	protected void createOptions() {
		MenuTab tab = new MenuTab();
		tab.addOption(this.summary = new MenuOption("friendareas.summary"));
		tab.addOption(this.moves = new MenuOption("menu.moves"));
		tab.addOption(this.leave = new MenuOption("ui.friend.leave"));
		tab.addOption(this.back = new MenuOption("general.back"));
		this.tabs.add(tab);
	}

	@Override
	protected Rectangle mainWindowDimensions() {
		Rectangle r = super.mainWindowDimensions();
		Rectangle parent = this.parent.getMainWindow().dimensions;
		return new Rectangle((int) (parent.getMaxX() + 10), parent.y, r.width, r.height);
	}

	@Override
	protected void onExit() {
		Persistence.stateManager.setState(this.parent);
	}

	@Override
	protected void onOptionSelected(MenuOption option) {
		if (option == this.summary)
			Persistence.stateManager.setState(
					TeamMenuState.createSummaryState(this.background, this, null, this.pokemon).setOpaque(true));
		else if (option == this.moves)
			Persistence.stateManager
					.setState(new MovesMenuState(this, this.background, false, this.pokemon).setOpaque(true));
		else if (option == this.back)
			this.onExit();
	}

}

package com.darkxell.client.state.menu.freezone;

import java.awt.Rectangle;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.menus.TeamMenuState;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.zones.FreezoneInfo;

public class FriendSelectionOptionState extends OptionSelectionMenuState {

    public final FriendSelectionState parent;
    public final Pokemon pokemon;
    private MenuOption visit, summary, exit;

    public FriendSelectionOptionState(FriendSelectionState parent, Pokemon pokemon) {
        super(parent, false);
        this.parent = parent;
        this.pokemon = pokemon;
        this.createOptions();
    }

    @Override
    protected void createOptions() {
        MenuTab tab = new MenuTab();
        tab.addOption(this.visit = new MenuOption("friendareas.visit"));
        tab.addOption(this.summary = new MenuOption("friendareas.summary"));
        tab.addOption(this.exit = new MenuOption("general.back"));
        this.tabs.add(tab);
    }

    @Override
    protected Rectangle mainWindowDimensions() {
        Rectangle r = super.mainWindowDimensions();
        Rectangle n = this.parent.nameWindow().dimensions;
        r.x = n.x;
        r.y = (int) (n.getMaxY() + 10);
        return r;
    }

    @Override
    protected void onExit() {
        Persistence.stateManager.setState(this.parent);
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        if (option == this.visit)
            StateManager.setExploreState(FreezoneInfo.find(this.pokemon.species().friendAreaID), null, -1, -1, true);
        else if (option == this.summary)
            Persistence.stateManager
                    .setState(TeamMenuState.createSummaryState(this.parent.background, this, this.pokemon));
        else if (option == this.exit)
            this.onExit();
    }

}

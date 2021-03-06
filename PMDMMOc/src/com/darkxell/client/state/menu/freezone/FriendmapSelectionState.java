package com.darkxell.client.state.menu.freezone;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.MenuOption;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.zones.FreezoneInfo;

public class FriendmapSelectionState extends OptionSelectionMenuState<MenuOption> {

    public static class MenuZoneOption extends MenuOption {
        public final FreezoneInfo info;

        public MenuZoneOption(FreezoneInfo info) {
            super(info.getName());
            this.info = info;
        }
    }

    private ArrayList<FreezoneInfo> destinations;
    private MenuOption exit;
    private FriendAreaSelectionMapState parent;
    private Message title;

    public FriendmapSelectionState(FriendAreaSelectionMapState parent, ArrayList<FreezoneInfo> destinations,
            Message title) {
        super(parent, false);
        this.parent = parent;
        this.title = title;
        this.destinations = destinations;
        this.createOptions();
    }

    @Override
    protected void createOptions() {
        MenuTab<MenuOption> tab = new MenuTab<>(title);
        for (FreezoneInfo destination : destinations)
            tab.addOption(new MenuZoneOption(destination));
        tab.addOption(this.exit = new MenuOption("general.back"));
        this.tabs.add(tab);
    }

    @Override
    protected Rectangle mainWindowDimensions() {
        Rectangle r = super.mainWindowDimensions();
        if (!parent.shouldInvertUI())
            r.x = PrincipalMainState.displayWidth - r.width - r.x;
        return r;
    }

    @Override
    protected void onExit() {
        Persistence.stateManager.setState(this.parent);
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        if (option == exit)
            this.onExit();
        else if (option instanceof MenuZoneOption) {
            FreezoneInfo info = ((MenuZoneOption) option).info;
            StateManager.setExploreState(info, null, -1, -1, true);
        }
    }

}

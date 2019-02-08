package com.darkxell.client.state.menu.freezone;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;

public class MissionBoardSelectionState extends OptionSelectionMenuState {

    public MissionBoardSelectionState(AbstractState bg) {
        super(bg);
        this.createOptions();
        this.parent = bg;
    }

    private AbstractState parent;

    private MenuOption board;
    private MenuOption mission;
    private MenuOption exit;

    @Override
    protected void createOptions() {
        MenuTab tab = new MenuTab();
        tab.addOption(this.board = new MenuOption("missionbillboard.board"));
        tab.addOption(this.mission = new MenuOption("missionbillboard.missions"));
        tab.addOption(this.exit = new MenuOption("general.back"));
        this.tabs.add(tab);
    }

    @Override
    protected void onExit() {
        Persistence.stateManager.setState(parent);
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        if (option == exit)
            this.onExit();
        else if (option == board)
            Persistence.stateManager.setState(new MissionBoardState(parent));
        else if (option == mission)
            Persistence.stateManager.setState(new MyMissionsState(parent, this));
    }

}

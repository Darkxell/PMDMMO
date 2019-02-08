package com.darkxell.client.state.menu.freezone;

import java.util.HashMap;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.mission.InvalidParammetersException;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;

public class MyMissionsState extends OptionSelectionMenuState {

    /**
     * Creates a new MyMissionsState.
     * 
     * @param backgroundState the background used to draw what's behind the menu
     * @param parent          the parent state that will come back if you exit this state
     */
    public MyMissionsState(AbstractGraphiclayer backgroundState, AbstractState parent) {
        super(backgroundState);
        this.createOptions();
        this.callback = parent;
    }

    private AbstractState callback = null;

    private MenuOption exit;
    private HashMap<MenuOption, Mission> missions = new HashMap<>();

    @Override
    protected void createOptions() {
        this.tabs.add(generateTab());
    }

    private MenuTab generateTab() {
        MenuTab tab = new MenuTab(new Message("mission.job.list"));
        missions.clear();
        for (int i = 0; i < Persistence.player.getMissions().size(); i++)
            try {
                Mission m = new Mission(Persistence.player.getMissions().get(i));
                String summary = m.summary();
                MenuOption opt = new MenuOption(summary);
                tab.addOption(opt);
                missions.put(opt, m);
            } catch (InvalidParammetersException e) {
                Logger.e("Couldn't add the following mission to mymissionsstate : "
                        + Persistence.player.getMissions().get(i));
            }
        tab.addOption(this.exit = new MenuOption("general.back"));
        return tab;
    }

    public void refresh() {
        if (this.tabs.size() == 0)
            this.tabs.add(generateTab());
        else {
            MenuTab toremove = this.tabs.get(0);
            this.tabs.add(generateTab());
            this.tabs.remove(toremove);
        }
    }

    @Override
    protected void onExit() {
        Persistence.stateManager.setState(callback);
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        if (option == exit)
            this.onExit();
        else
            Persistence.stateManager.setState(new MyMissionsChoiceState(this, this.missions.get(option)));
    }

}

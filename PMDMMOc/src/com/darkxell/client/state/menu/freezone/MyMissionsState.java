package com.darkxell.client.state.menu.freezone;

import java.awt.Rectangle;
import java.util.HashMap;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.MenuOption;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.SimpleActionSelectionMenuState;
import com.darkxell.client.state.menu.SimpleActionSelectionMenuState.ActionMenuOption;
import com.darkxell.client.state.menu.SimpleActionSelectionMenuState.ActionSelectionParent;
import com.darkxell.common.mission.InvalidParammetersException;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class MyMissionsState extends OptionSelectionMenuState<MenuOption> implements ActionSelectionParent {

    private AbstractState callback = null;
    private MenuOption exit;
    private HashMap<MenuOption, Mission> missions = new HashMap<>();

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

    @Override
    public void actionSelected(ActionMenuOption option) {
        final int CHECK = 0, DELETE = 1, BACK = 2;

        if (option == null)
            Persistence.stateManager.setState(this);
        else {
            Mission mission = this.missions.get(this.currentOption());
            switch (option.index) {
            case CHECK:
                Persistence.stateManager
                        .setState(new MissionDetailsState((AbstractState) this.background, mission, this));
                break;
            case DELETE:
                Logger.i("Deleting mission : " + mission);
                JsonObject message = Json.object();
                message.add("action", "deletemission");
                message.add("mission", mission.toString());
                Persistence.socketendpoint.sendMessage(message.toString());
                if (Persistence.player.getMissions().remove(mission.toString())) {
                    this.refresh();
                    Persistence.stateManager.setState(this);
                } else
                    Logger.e("Could not delete mission : " + mission);
                break;
            case BACK:
                Persistence.stateManager.setState(this);
                break;
            }
        }
    }

    @Override
    public Rectangle actionSelectionWindowDimensions(Rectangle defaultDimensions) {
        defaultDimensions.x = PrincipalMainState.displayWidth - defaultDimensions.width - defaultDimensions.x;
        defaultDimensions.y *= 2;
        return defaultDimensions;
    }

    @Override
    protected void createOptions() {
        this.tabs.add(generateTab());
    }

    private MenuTab<MenuOption> generateTab() {
        MenuTab<MenuOption> tab = new MenuTab<>(new Message("mission.job.list"));
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

    @Override
    protected void onExit() {
        Persistence.stateManager.setState(callback);
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        if (option == exit)
            this.onExit();
        else {
            SimpleActionSelectionMenuState state = new SimpleActionSelectionMenuState(this, this, "mission.job.check",
                    "mission.job.delete", "general.back");
            state.setOpaque(this.isOpaque());
            Persistence.stateManager.setState(state);
        }
    }

    public void refresh() {
        if (this.tabs.size() == 0)
            this.tabs.add(generateTab());
        else {
            MenuTab<MenuOption> toremove = this.tabs.get(0);
            this.tabs.add(generateTab());
            this.tabs.remove(toremove);
            this.onTabChanged(this.tabs.get(0));
        }
    }

}

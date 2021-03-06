package com.darkxell.client.state.menu.dungeon;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.MenuOption;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.event.EventSource.BaseEventSource;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.event.dungeon.NextFloorEvent;

public class StairMenuState extends OptionSelectionMenuState<MenuOption> {
    private MenuOption proceed;

    public StairMenuState() {
        super(Persistence.dungeonState);
        this.createOptions();
    }

    @Override
    protected void createOptions() {
        this.tabs.add(new MenuTab<>("stairs.title").addOption(this.proceed = new MenuOption("stairs.proceed"))
                .addOption(new MenuOption("ui.cancel")));
    }

    @Override
    protected void onExit() {
        Persistence.stateManager.setState((AbstractState) this.background);
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        this.onExit();
        if (option == this.proceed)
            if (Persistence.floor.id == Persistence.dungeon.dungeon().getFloorCount())
                Persistence.eventProcessor().processEvent(
                        new DungeonExitEvent(Persistence.floor, BaseEventSource.PLAYER_ACTION, Persistence.player)
                                .setPAE());
            else
                Persistence.eventProcessor().processEvent(
                        new NextFloorEvent(Persistence.floor, BaseEventSource.PLAYER_ACTION, Persistence.player)
                                .setPAE());
    }

}

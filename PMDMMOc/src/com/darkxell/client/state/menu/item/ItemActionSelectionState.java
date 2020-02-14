package com.darkxell.client.state.menu.item;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.launchable.messagehandlers.ItemActionHandler.ItemActionMessageHandler;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.MenuOption;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.item.ItemAction;
import com.eclipsesource.json.JsonObject;

public class ItemActionSelectionState extends OptionSelectionMenuState<MenuOption> implements ItemActionMessageHandler {

    private ArrayList<ItemAction> actions;
    public final ItemActionSource actionSource;

    public ItemActionSelectionState(AbstractState backgroundState, ItemActionSource actionSource,
            ArrayList<ItemAction> actions) {
        super(backgroundState);
        this.actionSource = actionSource;
        this.actions = actions;

        ItemAction.sort(this.actions);
        this.createOptions();
    }

    @Override
    protected void createOptions() {
        MenuTab<MenuOption> tab = new MenuTab<>();
        for (ItemAction action : this.actions)
            tab.addOption(new MenuOption(action.getName(this.actionSource.selectedItem())));
        this.tabs.add(tab);
    }

    @Override
    public void handleMessage(JsonObject message) {
        if (this.actionSource instanceof ItemActionMessageHandler)
            ((ItemActionMessageHandler) this.actionSource).handleMessage(message);
    }

    @Override
    protected Rectangle mainWindowDimensions() {
        Rectangle r = super.mainWindowDimensions();
        Point p = new Point(r.x, r.y);
        if (this.background instanceof ItemContainersMenuState)
            p = ((ItemContainersMenuState) this.background).actionSelectionWindowLocation();
        return new Rectangle(p.x, p.y, r.width, r.height);
    }

    @Override
    protected void onExit() {
        Persistence.stateManager.setState((AbstractState) this.background);
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        this.actionSource.performAction(this.actions.get(this.currentTab().options().indexOf(option)));
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        // If background state hasn't been drawn yet.
        if (this.getMainWindow() == null && this.background instanceof ItemContainersMenuState
                && ((ItemContainersMenuState) this.background).getMainWindow() == null) {
            Shape c = g.getClip();
            g.setClip(0, 0, 0, 0);
            this.background.render(g, width, height);
            g.setClip(c);
        }

        super.render(g, width, height);
    }

}

package com.darkxell.client.state.dialog.friendarea;

import java.awt.Point;
import java.awt.Rectangle;

import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.menu.MenuOption;
import com.darkxell.client.state.menu.OptionSelectionMenuState;

public class BuyFriendAreaActionSelectionState extends OptionSelectionMenuState<MenuOption> {

    public enum BuyFriendAreaAction {
        BACK,
        BUY,
        INFO;
    }

    private MenuOption buy, info, back;
    public final BuyFriendAreaMenuState parent;

    public BuyFriendAreaActionSelectionState(AbstractGraphiclayer backgroundState, BuyFriendAreaMenuState parent) {
        super(backgroundState);
        this.parent = parent;
        this.createOptions();
    }

    @Override
    protected void createOptions() {
        MenuTab<MenuOption> tab = new MenuTab<>();
        tab.addOption(this.buy = new MenuOption("dialog.friendareas.buy"));
        tab.addOption(this.info = new MenuOption("general.info"));
        tab.addOption(this.back = new MenuOption("general.back"));
        this.tabs.add(tab);
    }

    @Override
    protected Rectangle mainWindowDimensions() {
        Rectangle r = super.mainWindowDimensions();
        Point p = this.parent.actionSelectionWindowLocation();
        return new Rectangle(p.x, p.y, r.width, r.height);
    }

    @Override
    protected void onExit() {
        this.onOptionSelected(this.back);
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        BuyFriendAreaAction action = BuyFriendAreaAction.BACK;
        if (option == this.buy)
            action = BuyFriendAreaAction.BUY;
        else if (option == this.info)
            action = BuyFriendAreaAction.INFO;
        this.parent.onActionSelected(action);
    }

}

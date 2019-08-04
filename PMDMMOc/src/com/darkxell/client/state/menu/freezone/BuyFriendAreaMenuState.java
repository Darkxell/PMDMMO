package com.darkxell.client.state.menu.freezone;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.dialog.friendarea.FriendAreaShopDialog;
import com.darkxell.client.state.dialog.friendarea.FriendAreaSummaryWindow;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.zones.FriendArea;
import com.darkxell.common.zones.FriendAreaAcquisition.BuyableFriendArea;

public class BuyFriendAreaMenuState extends OptionSelectionMenuState {

    public static class FriendAreaMenuOption extends MenuOption {

        public final FriendArea area;

        public FriendAreaMenuOption(FriendArea area) {
            super(area.getName(), Persistence.player.moneyInBag() >= area.buyPrice());
            this.area = area;
        }
    }

    public final FriendAreaShopDialog dialog;
    private FriendAreaSummaryWindow summaryWindow;

    public BuyFriendAreaMenuState(AbstractGraphiclayer backgroundState, FriendAreaShopDialog dialog, boolean isOpaque) {
        super(backgroundState, isOpaque);
        this.dialog = dialog;
        this.createOptions();
        this.reloadSummaryWindow();
    }

    @Override
    protected void createOptions() {
        ArrayList<FriendArea> friendareas = new ArrayList<>();
        for (FriendArea area : FriendArea.values())
            if (area.acquisition instanceof BuyableFriendArea
                    && ((BuyableFriendArea) area.acquisition).minStorypos <= Persistence.player.storyPosition())
                friendareas.add(area);
        friendareas.removeIf(area -> Persistence.player.friendAreas.contains(area));

        MenuTab tab = new MenuTab("ui.friendarea.title");
        this.tabs.add(tab);
        int count = 0;
        for (FriendArea area : friendareas) {
            if (count >= 10) {
                tab = new MenuTab("ui.friendarea.title");
                this.tabs.add(tab);
                count = 0;
            }
            tab.addOption(new FriendAreaMenuOption(area));
            ++count;
        }
    }

    @Override
    protected void onExit() {
        this.dialog.resume(FriendAreaShopDialog.MENU_ACTION);
    }

    @Override
    protected void onOptionChanged(MenuOption option) {
        super.onOptionChanged(option);
        this.reloadSummaryWindow();
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        // TODO Auto-generated method stub
        this.onExit();
    }

    private void reloadSummaryWindow() {
        Rectangle dimensions = this.mainWindowDimensions();
        this.summaryWindow = new FriendAreaSummaryWindow(((FriendAreaMenuOption) this.tabs.get(0).options()[0]).area,
                new Rectangle((int) (dimensions.getMaxX() + 10), dimensions.y,
                        PrincipalMainState.displayWidth - 40 - dimensions.width,
                        PrincipalMainState.displayHeight / 2 - 5));
        this.summaryWindow.isOpaque = true;
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        super.render(g, width, height);
        this.summaryWindow.render(g, this.selectedArea().getName(), width, height);
    }

    private FriendArea selectedArea() {
        return ((FriendAreaMenuOption) this.currentOption()).area;
    }

}

package com.darkxell.client.state.dialog.friendarea;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.PokemonDialogScreen;
import com.darkxell.client.state.dialog.friendarea.BuyFriendAreaActionSelectionState.BuyFriendAreaAction;
import com.darkxell.client.state.dialog.friendarea.BuyFriendAreaMenuState.FriendAreaMenuOption;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.MenuOption;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.state.menu.components.TextWindow;
import com.darkxell.client.state.menu.freezone.FriendAreaInfoState;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.zones.FriendArea;
import com.darkxell.common.zones.FriendAreaAcquisition.BuyableFriendArea;
import com.eclipsesource.json.JsonObject;

public class BuyFriendAreaMenuState extends OptionSelectionMenuState<FriendAreaMenuOption> {

    public static class FriendAreaMenuOption extends MenuOption {

        public final FriendArea area;

        public FriendAreaMenuOption(FriendArea area) {
            super(area.getName(), Persistence.player.moneyInBag() >= area.buyPrice());
            this.area = area;
        }
    }

    public static ArrayList<FriendArea> getBuyableFriendAreas() {
        ArrayList<FriendArea> friendareas = new ArrayList<>();
        for (FriendArea area : FriendArea.values())
            if (area.acquisition instanceof BuyableFriendArea
                    && ((BuyableFriendArea) area.acquisition).minStorypos <= Persistence.player.storyPosition())
                friendareas.add(area);
        friendareas.removeIf(area -> Persistence.player.friendAreas.contains(area));
        return friendareas;
    }

    private FriendArea buying = null;
    public final FriendAreaShopDialog dialog;
    private TextWindow moneyWindow, buyPriceWindow;
    private Message moneyWindowTitle, buyPriceWindowTitle;

    public BuyFriendAreaMenuState(AbstractGraphiclayer backgroundState, FriendAreaShopDialog dialog, boolean isOpaque) {
        super(backgroundState, isOpaque);
        this.dialog = dialog;
        this.moneyWindowTitle = new Message("dialog.shop.money");
        this.buyPriceWindowTitle = new Message("dialog.shop.buyprice");
        this.createOptions();
        this.reloadWindows();
    }

    public Point actionSelectionWindowLocation() {
        return new Point(this.buyPriceWindow.dimensions.x, (int) (this.buyPriceWindow.dimensions.getMaxY() + 5));
    }

    public FriendArea buyingArea() {
        return this.buying;
    }

    @Override
    protected void createOptions() {
        ArrayList<FriendArea> friendareas = getBuyableFriendAreas();

        MenuTab<FriendAreaMenuOption> tab = new MenuTab<>("ui.friendarea.title");
        this.tabs.add(tab);
        int count = 0;
        for (FriendArea area : friendareas) {
            if (count >= 10) {
                tab = new MenuTab<>("ui.friendarea.title");
                this.tabs.add(tab);
                count = 0;
            }
            tab.addOption(new FriendAreaMenuOption(area));
            ++count;
        }
    }

    public void handleMessage(JsonObject message) {
        if (message.getString("action", "nothing").equals("buyfriendarea")) {
            if (message.getString("result", "error").equals("success"))
                this.onBuySuccess();
            else {
                Logger.e("Client/Server desync. Please reboot game.");
                Persistence.stateManager
                        .setState(new DialogState(this.background, finish -> Persistence.stateManager.setState(this),
                                new DialogScreen(new Message("ui.desync"))).setOpaque(this.isOpaque()));
            }
        }
    }

    public void onActionSelected(BuyFriendAreaAction action) {
        if (action == BuyFriendAreaAction.BUY) {
            if (Persistence.socketendpoint.connectionStatus() != GameSocketEndpoint.CONNECTED)
                this.onBuySuccess();
            else {
                Persistence.isCommunicating = true;
                JsonObject message = new JsonObject().add("action", "buyfriendarea").add("area",
                        this.selectedArea().id());
                Persistence.socketendpoint.sendMessage(message.toString());
            }
        } else if (action == BuyFriendAreaAction.INFO)
            Persistence.stateManager.setState(
                    new FriendAreaInfoState(this, this.background, this.selectedArea()).setOpaque(this.isOpaque()));
        else
            Persistence.stateManager.setState(this);
    }

    private void onBuySuccess() {
        Persistence.player.friendAreas.add(this.selectedArea());
        Persistence.player.setMoneyInBag(Persistence.player.moneyInBag() - this.selectedArea().buyPrice());
        Persistence.stateManager.setState(new DialogState(this.background,
                finish -> Persistence.stateManager
                        .setState(new BuyFriendAreaMenuState(this.background, this.dialog, this.isOpaque())),
                new PokemonDialogScreen(this.dialog.shopkeeper, new Message("dialog.friendareas.buying")),
                new PokemonDialogScreen(this.dialog.shopkeeper, new Message("dialog.friendareas.buying.done")
                        .addReplacement("<area>", this.selectedArea().getName()))).setOpaque(this.isOpaque()));
    }

    @Override
    protected void onExit() {
        this.dialog.unpause();
    }

    @Override
    protected void onOptionChanged(FriendAreaMenuOption option) {
        super.onOptionChanged(option);
        this.reloadWindows();
    }

    @Override
    protected void onOptionSelected(FriendAreaMenuOption option) {
        Persistence.stateManager.setState(new BuyFriendAreaActionSelectionState(this, this).setOpaque(this.isOpaque()));
    }

    private void reloadWindows() {
        Rectangle dimensions = this.mainWindowDimensions();
        final int rightWindowsX = (int) (dimensions.getMaxX() + 10),
                rightWindowsWidth = PrincipalMainState.displayWidth - 40 - dimensions.width;

        this.buyPriceWindow = new TextWindow(
                new Rectangle(rightWindowsX, dimensions.y, rightWindowsWidth / 2 - 5,
                        TextRenderer.height() + TextRenderer.lineSpacing() * 2 + MenuWindow.MARGIN_Y * 2),
                new Message(Integer.toString(this.selectedArea().buyPrice()), false), false);

        this.moneyWindow = new TextWindow(
                new Rectangle(rightWindowsX + rightWindowsWidth / 2 + 5, dimensions.y,
                        this.buyPriceWindow.dimensions.width, this.buyPriceWindow.dimensions.height),
                new Message(Long.toString(Persistence.player.moneyInBag()), false), false);

        this.buyPriceWindow.isOpaque = this.moneyWindow.isOpaque = true;
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        super.render(g, width, height);
        this.buyPriceWindow.render(g, this.buyPriceWindowTitle, width, height);
        this.moneyWindow.render(g, this.moneyWindowTitle, width, height);
    }

    public FriendArea selectedArea() {
        return this.currentOption().area;
    }

}

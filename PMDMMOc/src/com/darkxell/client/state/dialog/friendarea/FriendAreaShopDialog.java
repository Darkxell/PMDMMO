package com.darkxell.client.state.dialog.friendarea;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.ComplexDialog;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.client.state.dialog.PokemonDialogScreen.DialogPortraitLocation;
import com.darkxell.client.state.menu.freezone.BuyFriendAreaMenuState;
import com.darkxell.common.Registries;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class FriendAreaShopDialog extends ComplexDialog {

    public static final int BUY = 0, CHECK = 1, BACK = 2;
    public static final byte MENU_ACTION = 0, DIALOG_BUY = 1, DIALOG_ALL_BOUGHT = 2, DIALOG_BUY_SUCCESS = 3,
            MENU_CHECK = 4, MENU_CHECK_RESULT = 5, DIALOG_GOODBYE = 10;

    private byte dialogToShow = -1;
    private Message[] options;
    private PokemonSpecies shopkeeper;

    public FriendAreaShopDialog(AbstractGraphiclayer background) {
        super(background);
        this.shopkeeper = Registries.species().find(40);
        this.options = new Message[] { new Message("dialog.friendareas.buy"), new Message("dialog.friendareas.check"),
                new Message("general.back") };
    }

    private DialogState actionSelection(boolean isFirst) {
        return this.newDialog(new OptionDialogScreen(this.shopkeeper,
                new Message(isFirst ? "dialog.friendareas.welcome" : "dialog.friendareas.welcome2"),
                DialogPortraitLocation.BOTTOM_RIGHT, this.options).setID(MENU_ACTION)).setOpaque(true);
    }

    @Override
    public DialogState firstState() {
        return this.actionSelection(true);
    }

    @Override
    public ComplexDialogAction nextAction(DialogState previous) {
        DialogScreen screen = previous.currentScreen();
        switch (screen.id) {
        case DIALOG_BUY:
            return ComplexDialogAction.PAUSE;
        case DIALOG_GOODBYE:
            return ComplexDialogAction.TERMINATE;
        }
        return ComplexDialogAction.NEW_DIALOG;
    }

    @Override
    public DialogState nextState(DialogState previous) {
        DialogScreen screen = previous.currentScreen();
        if (screen.id == MENU_ACTION) {
            int option = ((OptionDialogScreen) screen).chosenIndex();
            if (option == BUY)
                return this.newDialog(new DialogScreen(new Message("dialog.friendareas.buy.ask")).setID(DIALOG_BUY))
                        .setOpaque(true);
        }
        return this.actionSelection(false);
    }

    @Override
    protected void onDialogFinished(DialogState dialog) {
        DialogScreen screen = dialog.currentScreen();
        if (screen.id == DIALOG_BUY)
            Persistence.stateManager.setState(new BuyFriendAreaMenuState(this.background, this, true));
    }

    @Override
    public AbstractState onFinish(DialogState lastState) {
        return (AbstractState) this.background;
    }

    public void resume(byte dialogToShow) {
        this.dialogToShow = dialogToShow;
        this.unpause();
    }

}

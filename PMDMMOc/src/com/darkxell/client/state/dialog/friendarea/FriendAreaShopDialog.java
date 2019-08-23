package com.darkxell.client.state.dialog.friendarea;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.ComplexDialog;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.client.state.dialog.PokemonDialogScreen;
import com.darkxell.client.state.dialog.PokemonDialogScreen.DialogPortraitLocation;
import com.darkxell.common.Registries;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class FriendAreaShopDialog extends ComplexDialog {

    public static final int BUY = 0, CHECK = 1, BACK = 2;
    public static final byte MENU_ACTION = 0, DIALOG_BUY = 1, DIALOG_ALL_BOUGHT = 2, DIALOG_CHECK = 3,
            DIALOG_GOODBYE = 10;

    public final ArrayList<PokemonSpecies> knownPokemon;
    private Message[] options;
    public final PokemonSpecies shopkeeper;

    public FriendAreaShopDialog(AbstractGraphiclayer background) {
        super(background);
        this.shopkeeper = Registries.species().find(40);
        this.options = new Message[] { new Message("dialog.friendareas.buy"), new Message("dialog.friendareas.check"),
                new Message("general.back") };
        this.knownPokemon = Persistence.player.getKnownPokemon();
    }

    private DialogState actionSelection(boolean isFirst) {
        return this.newDialog(new OptionDialogScreen(this.shopkeeper,
                new Message(isFirst ? "dialog.friendareas.welcome" : "dialog.friendareas.welcome2"),
                DialogPortraitLocation.BOTTOM_LEFT, this.options).setID(MENU_ACTION)).setOpaque(true);
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
        case DIALOG_CHECK:
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
                return this.newDialog(new PokemonDialogScreen(this.shopkeeper, new Message("dialog.friendareas.ask"))
                        .setID(DIALOG_BUY)).setOpaque(true);
            else if (option == CHECK)
                return this
                        .newDialog(new PokemonDialogScreen(this.shopkeeper, new Message("dialog.friendareas.check.ask"))
                                .setID(DIALOG_CHECK))
                        .setOpaque(true);
            else if (option == BACK)
                return this
                        .newDialog(new PokemonDialogScreen(this.shopkeeper, new Message("dialog.friendareas.goodbye"))
                                .setID(DIALOG_GOODBYE))
                        .setOpaque(true);
        }
        return this.actionSelection(false);
    }

    @Override
    protected void onDialogFinished(DialogState dialog) {
        DialogScreen screen = dialog.currentScreen();
        if (screen.id == DIALOG_BUY)
            Persistence.stateManager.setState(new BuyFriendAreaMenuState(this.background, this, true));
        if (screen.id == DIALOG_CHECK)
            Persistence.stateManager.setState(new CheckFriendsMenuState(this.background, this, true));
    }

    @Override
    public AbstractState onFinish(DialogState lastState) {
        return (AbstractState) this.background;
    }

}

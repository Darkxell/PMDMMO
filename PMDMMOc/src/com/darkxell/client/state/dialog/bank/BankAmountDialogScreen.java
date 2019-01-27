package com.darkxell.client.state.dialog.bank;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.graphics.TextRenderer;
import com.darkxell.client.state.dialog.PokemonDialogScreen;
import com.darkxell.client.state.menu.components.IntegerSelectionState;
import com.darkxell.client.state.menu.components.IntegerSelectionState.IntegerSelectionListener;
import com.darkxell.client.state.menu.components.OptionSelectionWindow;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.awt.*;

public class BankAmountDialogScreen extends PokemonDialogScreen implements IntegerSelectionListener {
    public final long bag, bank;
    public final boolean depositing;
    private boolean showingOptions = false;
    private BankMenuWindow summaryWindow;

    public BankAmountDialogScreen(PokemonSpecies shopkeeper, long bag, long bank, boolean depositing) {
        super(shopkeeper, new Message("dialog.bank.howmany." + (depositing ? "deposit" : "withdraw")));
        this.bag = bag;
        this.bank = bank;
        this.depositing = depositing;
    }

    @Override
    public void onIntegerSelected(long selection) {
        if (selection == -1) {
            ((BankDialog) Persistence.currentDialog).onSelectionCancel();
            return;
        }

        if (!this.depositing) {
            selection *= -1;
        }
        Persistence.isCommunicating = true;

        if (Persistence.socketendpoint.connectionStatus() != GameSocketEndpoint.CONNECTED) {
            long bag = Persistence.player.moneyInBag() - selection, bank = Persistence.player.moneyInBank() + selection;
            ((BankDialog) Persistence.currentDialog).onConfirmReceived(bag, bank);
        } else {
            JsonObject root = Json.object();
            root.add("action", "bankaction");
            root.add("money", selection);
            Persistence.socketendpoint.sendMessage(root.toString());
        }
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        super.render(g, width, height);
        if (this.showingOptions) {
            if (this.summaryWindow == null) {
                Rectangle dialog = this.parentState.dialogBox();
                this.summaryWindow = new BankMenuWindow(new Rectangle(dialog.x, 20, dialog.width,
                        TextRenderer.height() + OptionSelectionWindow.MARGIN_Y * 2), this.bag, this.bank);
            }
            this.summaryWindow.render(g, null, width, height);
        }
    }

    @Override
    public boolean requestNextMessage() {
        if (!this.showingOptions) {
            this.showingOptions = true;
            long start = this.depositing ? this.bag : this.bank, min = 0;
            Persistence.stateManager.setState(
                    new IntegerSelectionState(this.parentState, this.parentState, this, min, start, start));
            return false;
        } else {
            return super.requestNextMessage();
        }
    }
}

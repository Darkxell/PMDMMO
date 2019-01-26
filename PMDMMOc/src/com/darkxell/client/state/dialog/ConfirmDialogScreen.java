package com.darkxell.client.state.dialog;

import com.darkxell.common.util.language.Message;

public class ConfirmDialogScreen extends OptionDialogScreen {
    public ConfirmDialogScreen(Message message) {
        super(message, new Message("ui.yes"), new Message("ui.no"));
    }

    public boolean hasConfirmed() {
        return this.chosenIndex() == 0;
    }
}

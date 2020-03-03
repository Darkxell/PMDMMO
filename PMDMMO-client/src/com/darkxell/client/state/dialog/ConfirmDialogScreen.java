package com.darkxell.client.state.dialog;

import com.darkxell.client.resources.image.pokemon.portrait.PortraitEmotion;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class ConfirmDialogScreen extends OptionDialogScreen {

    public ConfirmDialogScreen(Message message) {
        super(message, new Message("ui.yes"), new Message("ui.no"));
    }

    public ConfirmDialogScreen(Pokemon pokemon, Message message) {
        super(pokemon, message, DialogPortraitLocation.BOTTOM_LEFT, PortraitEmotion.Normal, new Message("ui.yes"),
                new Message("ui.no"));
    }

    public boolean hasConfirmed() {
        return this.chosenIndex() == 0;
    }
}

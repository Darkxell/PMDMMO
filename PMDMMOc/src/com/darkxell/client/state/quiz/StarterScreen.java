package com.darkxell.client.state.quiz;

import java.awt.Graphics2D;

import com.darkxell.client.resources.image.pokemon.portrait.AbstractPortraitSpriteset;
import com.darkxell.client.resources.image.pokemon.portrait.PortraitEmotion;
import com.darkxell.client.resources.image.pokemon.portrait.Portraits;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class StarterScreen extends OptionDialogScreen {

    public final Pokemon pokemon;

    public StarterScreen(Message message, Pokemon pokemon) {
        super(message, new Message("ui.yes"), new Message("ui.no"));
        this.pokemon = pokemon;
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        super.render(g, width, height);
        Portraits.drawPortrait(g, this.pokemon, PortraitEmotion.Happy,
                width / 2 - AbstractPortraitSpriteset.PORTRAIT_SIZE / 2, height / 2 - AbstractPortraitSpriteset.PORTRAIT_SIZE / 2);
    }

}

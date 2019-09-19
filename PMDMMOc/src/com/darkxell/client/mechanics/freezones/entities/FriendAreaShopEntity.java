package com.darkxell.client.mechanics.freezones.entities;

import java.util.Random;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.PokemonDialogScreen;
import com.darkxell.client.state.dialog.PokemonDialogScreen.DialogPortraitLocation;
import com.darkxell.client.state.dialog.friendarea.FriendAreaShopDialog;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.Util;
import com.darkxell.common.util.language.Message;

public class FriendAreaShopEntity extends FreezoneEntity {

    public FriendAreaShopEntity(double x, double y) {
        super(false, true, x, y);
    }

    @Override
    public void onInteract() {
        if (Persistence.player.storyPosition() <= Util.POST_WIGGLYTUFF_STORYPOS) {
            AbstractState state = Persistence.stateManager.getCurrentState();
            DialogScreen s = new PokemonDialogScreen(Registries.species().find(40).generate(new Random(), 0),
                    new Message("dialog.place.wigglytuff.1"), DialogPortraitLocation.BOTTOM_RIGHT);
            Persistence.stateManager.setState(
                    new DialogState(state, finish -> Persistence.stateManager.setState(state), s).setOpaque(true));
        } else
            new FriendAreaShopDialog(Persistence.stateManager.getCurrentState()).start();
    }

    @Override
    public void update() {
    }

}

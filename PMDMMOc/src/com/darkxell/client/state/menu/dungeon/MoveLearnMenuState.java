package com.darkxell.client.state.menu.dungeon;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.state.dialog.ConfirmDialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.components.MoveSelectionWindow;
import com.darkxell.client.state.menu.menus.MovesMenuState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.event.move.MoveDiscoveredEvent;
import com.darkxell.common.event.move.MoveLearnedEvent;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class MoveLearnMenuState extends MovesMenuState implements DialogEndListener {

    public final MoveDiscoveredEvent event;
    public final LearnedMove move;
    public final Pokemon pokemon;

    public MoveLearnMenuState(DungeonState parent, MoveDiscoveredEvent event) {
        super(parent, parent, true, event.pokemon);
        this.event = event;
        this.pokemon = this.event.pokemon;
        this.move = new LearnedMove(this.event.move.getID());
        this.canOrder = false;

        this.tabs.get(0).addOption(new MoveMenuOption(this.move, this.pokemon == Persistence.player.getTeamLeader()));
    }

    protected Message infoText() {
        return new Message("moves.info.learn").addReplacement("<key-ok>", KeyEvent.getKeyText(Key.ATTACK.keyValue()))
                .addReplacement("<key-info>", KeyEvent.getKeyText(Key.ROTATE.keyValue()))
                .addReplacement("<key-shift>", KeyEvent.getKeyText(Key.DIAGONAL.keyValue()));
    }

    @Override
    protected Rectangle mainWindowDimensions() {
        Rectangle r = super.mainWindowDimensions();
        return new Rectangle(r.x, r.y, r.width + MoveSelectionWindow.ppLength,
                r.height + (TextRenderer.height() + TextRenderer.lineSpacing()));
    }

    @Override
    public void onDialogEnd(DialogState dialog) {
        if (((ConfirmDialogScreen) dialog.getScreen(1)).hasConfirmed()) {
            Persistence.stateManager.setState(Persistence.dungeonState);
            if (this.optionIndex() < 4)
                Persistence.eventProcessor().processEvent(new MoveLearnedEvent(Persistence.floor, this.event,
                        this.pokemon, this.move.move(), this.optionIndex()));
            else
                Persistence.eventProcessor().processPending();
        } else
            Persistence.stateManager.setState(this);
    }

    @Override
    protected void onExit() {
    }

    @Override
    protected void onOptionSelected(MoveMenuOption option) {

        ConfirmDialogScreen confirm = new ConfirmDialogScreen(
                new Message("moves.forget").addReplacement("<pokemon>", this.pokemon.getNickname())
                        .addReplacement("<move>", option.move.move().name()));
        confirm.id = 1;
        DialogState dialog = new DialogState(this.background, this, confirm);
        Persistence.stateManager.setState(dialog);
    }

}

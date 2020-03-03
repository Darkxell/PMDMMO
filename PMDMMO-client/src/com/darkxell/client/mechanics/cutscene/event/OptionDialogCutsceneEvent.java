package com.darkxell.client.mechanics.cutscene.event;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.mechanics.cutscene.event.DialogCutsceneEvent.CutsceneDialogScreen;
import com.darkxell.client.model.cutscene.common.MessageModel;
import com.darkxell.client.model.cutscene.event.OptionDialogCutsceneEventModel;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class OptionDialogCutsceneEvent extends CutsceneEvent implements DialogEndListener {

    private int chosen = -1;
    private boolean isOver = false;
    private final OptionDialogCutsceneEventModel model;
    private CutsceneDialogScreen question;

    public OptionDialogCutsceneEvent(OptionDialogCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
        this.question = new CutsceneDialogScreen(this.model.getQuestion());
    }

    public int chosen() {
        return this.chosen;
    }

    @Override
    public boolean isOver() {
        return this.isOver;
    }

    @Override
    public void onDialogEnd(DialogState dialog) {
        this.isOver = true;
        this.chosen = ((OptionDialogScreen) dialog.getScreen(1)).chosenIndex();
        Persistence.stateManager.setState(Persistence.cutsceneState);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.isOver = false;
        CutscenePokemon pokemon = null;
        if (this.question.getTarget() != null) {
            CutsceneEntity e = this.context.parent().player.getEntity(this.question.getTarget());
            if (e instanceof CutscenePokemon)
                pokemon = (CutscenePokemon) e;
        }
        Message question = this.question.getMessage(pokemon);
        Pokemon instance = pokemon == null ? null : pokemon.toPokemon();

        Message[] options = new Message[this.model.getOptions().size()];
        for (int i = 0; i < options.length; ++i) {
            MessageModel m = this.model.getOptions().get(i);
            options[i] = new Message(m.getText(), m.getTranslate());
        }

        OptionDialogScreen screen = new OptionDialogScreen(instance, question, this.question.getPortraitLocation(),
                this.question.getEmotion(), options);
        screen.id = 1;
        DialogState state = new DialogState(Persistence.cutsceneState, this, screen);
        Persistence.stateManager.setState(state);
    }

}

package com.darkxell.client.mechanics.cutscene.event;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.model.cutscene.common.CutsceneDialogScreenModel;
import com.darkxell.client.model.cutscene.event.DialogCutsceneEventModel;
import com.darkxell.client.resources.image.pokemon.portrait.PortraitEmotion;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.client.state.dialog.NarratorDialogScreen;
import com.darkxell.client.state.dialog.PokemonDialogScreen;
import com.darkxell.client.state.dialog.PokemonDialogScreen.DialogPortraitLocation;
import com.darkxell.common.util.language.Message;

public class DialogCutsceneEvent extends CutsceneEvent implements DialogEndListener {

    public static class CutsceneDialogScreen {
        private Message message;
        private final CutsceneDialogScreenModel model;

        public CutsceneDialogScreen(CutsceneDialogScreenModel model) {
            this.model = model;
        }

        private void buildMessage(CutscenePokemon speaker) {
            this.message = new Message(this.model.getText(), this.model.getTranslate());
            this.message.addReplacement("<account-name>", Persistence.player.name());
            this.message.addReplacement("<player-name>", Persistence.player.getTeamLeader().getNickname());
            this.message.addReplacement("<player-type>", Persistence.player.getTeamLeader().species().formName());
            if (Persistence.player.allies.size() >= 1) {
                this.message.addReplacement("<partner-name>", Persistence.player.getMember(1).getNickname());
                this.message.addReplacement("<partner-type>", Persistence.player.getMember(1).species().formName());
            }
            if (speaker != null) {
                this.message.addReplacement("<speaker-name>", speaker.toPokemon().getNickname());
                this.message.addReplacement("<speaker-type>", speaker.toPokemon().species().formName());
            }
        }

        public PortraitEmotion getEmotion() {
            return this.model.getEmotion();
        }

        public Message getMessage(CutscenePokemon speaker) {
            if (this.message == null)
                this.buildMessage(speaker);
            return this.message;
        }

        public DialogPortraitLocation getPortraitLocation() {
            return this.model.getPortraitLocation();
        }

        public Integer getTarget() {
            return this.model.getTarget();
        }

        @Override
        public String toString() {
            return this.message.toString();
        }
    }

    private boolean isOver;
    private final DialogCutsceneEventModel model;
    private ArrayList<CutsceneDialogScreen> screens;

    public DialogCutsceneEvent(DialogCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
        this.isOver = false;
        this.screens = new ArrayList<>();
        this.model.getScreens().forEach(s -> this.screens.add(new CutsceneDialogScreen(s)));
    }

    @Override
    public boolean isOver() {
        return this.isOver;
    }

    @Override
    public void onDialogEnd(DialogState dialog) {
        Persistence.stateManager.setState(Persistence.cutsceneState);
        this.isOver = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        DialogScreen[] screens = new DialogScreen[this.screens.size()];
        int index = 0;
        this.isOver = false;
        for (CutsceneDialogScreen s : this.screens) {
            CutscenePokemon pokemon = null;
            if (s.getTarget() != null) {
                CutsceneEntity e = this.context.parent().player.getEntity(s.getTarget());
                if (e instanceof CutscenePokemon)
                    pokemon = (CutscenePokemon) e;
            }
            Message message = s.getMessage(pokemon);
            DialogScreen screen = pokemon == null ? new DialogScreen(message)
                    : new PokemonDialogScreen(pokemon.toPokemon(), message, s.getEmotion(), s.getPortraitLocation());
            if (this.model.getIsNarratorDialog()) {
                screen = new NarratorDialogScreen(message);
                ((NarratorDialogScreen) screen).forceBlackBackground = false;
            }
            screens[index++] = screen;
        }

        DialogState state = new DialogState(Persistence.cutsceneState, this, screens);
        Persistence.stateManager.setState(state);
    }

    @Override
    public String toString() {
        return this.displayID() + "Dialog: " + this.screens.get(0).message.toString() + "...";
    }

}

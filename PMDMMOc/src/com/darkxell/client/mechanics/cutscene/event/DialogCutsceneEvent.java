package com.darkxell.client.mechanics.cutscene.event;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait.PortraitEmotion;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.client.state.dialog.NarratorDialogScreen;
import com.darkxell.client.state.dialog.PokemonDialogScreen;
import com.darkxell.client.state.dialog.PokemonDialogScreen.DialogPortraitLocation;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.util.xml.XMLUtils;

public class DialogCutsceneEvent extends CutsceneEvent implements DialogEndListener {

    public static class CutsceneDialogScreen {
        public final PortraitEmotion emotion;
        boolean hasReplacements = false;
        public final Message message;
        public final int pokemon;
        public final DialogPortraitLocation portraitLocation;

        public CutsceneDialogScreen(Element xml) {
            this.message = new Message(xml.getText(), XMLUtils.getAttribute(xml, "translate", true));
            this.pokemon = XMLUtils.getAttribute(xml, "target", -1);
            String emot = XMLUtils.getAttribute(xml, "emotion", PortraitEmotion.Normal.name());
            PortraitEmotion e;
            try {
                e = PortraitEmotion.valueOf(emot);
            } catch (Exception e2) {
                e = PortraitEmotion.Normal;
            }
            this.emotion = e;
            this.portraitLocation = DialogPortraitLocation.valueOf(
                    XMLUtils.getAttribute(xml, "portrait-location", DialogPortraitLocation.BOTTOM_LEFT.name()));
        }

        public CutsceneDialogScreen(Message message, PortraitEmotion emotion, CutsceneEntity entity,
                DialogPortraitLocation portraitLocation) {
            this.message = message;
            this.emotion = emotion;
            this.pokemon = entity == null ? -1 : entity.id;
            this.portraitLocation = portraitLocation;
        }

        public CutsceneDialogScreen(String text, boolean translate, PortraitEmotion emotion, CutsceneEntity entity,
                DialogPortraitLocation portraitLocation) {
            this(new Message(text, translate), emotion, entity, portraitLocation);
        }

        void addReplacements(CutscenePokemon speaker) {
            this.hasReplacements = true;
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

        @Override
        public String toString() {
            return this.message.toString();
        }

        public Element toXML(String elementName) {
            Element root = new Element(elementName).setText(this.message.id);
            XMLUtils.setAttribute(root, "translate", this.message.shouldTranslate, true);
            XMLUtils.setAttribute(root, "emotion", this.emotion.name(), PortraitEmotion.Normal.name());
            XMLUtils.setAttribute(root, "target", this.pokemon, -1);
            XMLUtils.setAttribute(root, "portrait-location", this.portraitLocation.name(),
                    DialogPortraitLocation.BOTTOM_LEFT.name());
            return root;
        }
    }

    public final boolean isNarratorDialog;
    private boolean isOver;
    public List<CutsceneDialogScreen> screens;

    public DialogCutsceneEvent(Element xml, CutsceneContext context) {
        super(xml, CutsceneEventType.dialog, context);
        this.isNarratorDialog = XMLUtils.getAttribute(xml, "isnarrator", false);
        this.screens = new ArrayList<>();
        for (Element screen : xml.getChildren("dialogscreen", xml.getNamespace()))
            this.screens.add(new CutsceneDialogScreen(screen));
        this.isOver = false;
    }

    public DialogCutsceneEvent(int id, boolean isNarrator, List<CutsceneDialogScreen> screens) {
        super(id, CutsceneEventType.dialog);
        this.isNarratorDialog = isNarrator;
        this.screens = screens;
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
            CutsceneEntity e = this.context.parent().player.getEntity(s.pokemon);
            if (e != null && e instanceof CutscenePokemon)
                pokemon = (CutscenePokemon) e;
            if (!s.hasReplacements)
                s.addReplacements(pokemon);
            DialogScreen screen = pokemon == null ? new DialogScreen(s.message)
                    : new PokemonDialogScreen(pokemon.toPokemon(), s.message, s.emotion, s.portraitLocation);
            if (this.isNarratorDialog) {
                screen = new NarratorDialogScreen(s.message);
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

    @Override
    public Element toXML() {
        Element root = super.toXML();
        XMLUtils.setAttribute(root, "isnarrator", this.isNarratorDialog, false);
        for (CutsceneDialogScreen screen : this.screens)
            root.addContent(screen.toXML("dialogscreen"));
        return root;
    }

}

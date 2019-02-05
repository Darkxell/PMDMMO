package com.darkxell.client.mechanics.cutscene;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.event.AnimateCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.CameraCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DelayCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DespawnCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DialogCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DrawMapCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.FunctionCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.MoveCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.MusicCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.OptionDialogCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.OptionResultCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.RotateCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.SetAnimatedCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.SetStateCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.SoundCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.SpawnCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.WaitCutsceneEvent;
import com.darkxell.common.util.xml.XMLUtils;

public abstract class CutsceneEvent {

    public static enum CutsceneEventType {
        // Don't forget to also modify EditCutsceneController#onCreate().
        animate("Play animation"),
        camera("Move camera"),
        delay("Wait X ticks"),
        despawn("Despawn Entity"),
        dialog("Show Dialog"),
        drawmap("Draw Map"),
        function("Call function"),
        move("Move Entity"),
        music("Change soundtrack"),
        option("Select option"),
        optionresult("Trigger events by option result"),
        rotate("Rotate Entity"),
        setanimated("Animate Pokemon"),
        setstate("Set Pokemon State"),
        sound("Play sound"),
        spawn("Spawn Entity"),
        wait("Wait for events to finish");

        public final String description;

        private CutsceneEventType(String name) {
            this.description = name;
        }
    }

    public static CutsceneEvent create(Element xml, CutsceneContext context) {
        // Remember to add to Editor aswell (SelectEventTypeController#CutsceneEventType).
        switch (xml.getName()) {
        case "animate":
            return new AnimateCutsceneEvent(xml, context);

        case "camera":
            return new CameraCutsceneEvent(xml, context);

        case "delay":
            return new DelayCutsceneEvent(xml, context);

        case "despawn":
            return new DespawnCutsceneEvent(xml, context);

        case "dialog":
            return new DialogCutsceneEvent(xml, context);

        case "drawmap":
            return new DrawMapCutsceneEvent(xml, context);

        case "function":
            return new FunctionCutsceneEvent(xml, context);

        case "move":
            return new MoveCutsceneEvent(xml, context);

        case "music":
            return new MusicCutsceneEvent(xml, context);

        case "option":
            return new OptionDialogCutsceneEvent(xml, context);

        case "optionresult":
            return new OptionResultCutsceneEvent(xml, context);

        case "rotate":
            return new RotateCutsceneEvent(xml, context);

        case "setstate":
            return new SetStateCutsceneEvent(xml, context);

        case "setanimated":
            return new SetAnimatedCutsceneEvent(xml, context);

        case "sound":
            return new SoundCutsceneEvent(xml, context);

        case "spawn":
        case "spawnpokemon":
            return new SpawnCutsceneEvent(xml, context);

        case "wait":
            return new WaitCutsceneEvent(xml, context);

        default:
            return null;
        }
    }

    protected CutsceneContext context;
    public int id;
    public final CutsceneEventType type;

    public CutsceneEvent(Element xml, CutsceneEventType type, CutsceneContext context) {
        this.id = XMLUtils.getAttribute(xml, "eventid", -1);
        this.context = context;
        this.type = type;
    }

    public CutsceneEvent(int id, CutsceneEventType type) {
        this.id = id;
        this.type = type;
        this.context = null;
    }

    public String displayID() {
        return this.id == -1 ? "" : "(" + this.id + ") ";
    }

    public boolean isOver() {
        return true;
    }

    public void onFinish() {
    }

    public void onStart() {
    }

    public Element toXML() {
        Element root = new Element(this.type.name());
        XMLUtils.setAttribute(root, "eventid", this.id, -1);
        return root;
    }

    public void update() {
    }

}

package com.darkxell.client.mechanics.cutscene.event;

import java.util.ArrayList;
import java.util.HashSet;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;

public class WaitCutsceneEvent extends CutsceneEvent {

    public final boolean all;
    public ArrayList<CutsceneEvent> events;
    public HashSet<CutsceneEvent> remaining = new HashSet<>();

    public WaitCutsceneEvent(Element xml, CutsceneContext context) {
        super(xml, CutsceneEventType.wait, context);
        this.events = new ArrayList<>();
        for (Element event : xml.getChildren("event", xml.getNamespace()))
            try {
                int id = Integer.parseInt(event.getText());
                CutsceneEvent e = this.context.getEvent(id);
                if (e != null)
                    this.events.add(e);
            } catch (Exception ignored) {
            }
        this.all = this.events.isEmpty();
        if (this.events.isEmpty())
            this.events.addAll(this.context.availableEvents());
    }

    public WaitCutsceneEvent(int id, boolean all, ArrayList<CutsceneEvent> events) {
        super(id, CutsceneEventType.wait);
        this.all = all;
        this.events = events;
    }

    @Override
    public boolean isOver() {
        HashSet<CutsceneEvent> ended = new HashSet<>();
        for (CutsceneEvent event : this.remaining)
            if (event.isOver())
                ended.add(event);
        this.remaining.removeAll(ended);
        return this.remaining.isEmpty();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.remaining.clear();
        this.remaining.addAll(this.events);
    }

    @Override
    public String toString() {
        return this.displayID() + "Wait for " + (this.all ? "all" : this.events.size()) + " events";
    }

    @Override
    public Element toXML() {
        Element root = super.toXML();
        if (!this.all)
            for (CutsceneEvent e : this.events)
                root.addContent(new Element("event").setText(String.valueOf(e.id)));
        return root;
    }

}

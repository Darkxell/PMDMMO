package com.darkxell.client.mechanics.cutscene.event;

import java.util.ArrayList;
import java.util.HashSet;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.model.cutscene.event.WaitCutsceneEventModel;

public class WaitCutsceneEvent extends CutsceneEvent {

    public final boolean all;
    public ArrayList<CutsceneEvent> events;
    public HashSet<CutsceneEvent> remaining = new HashSet<>();

    public WaitCutsceneEvent(WaitCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.events = new ArrayList<>();
        for (Integer id : model.getEvents()) {
            CutsceneEvent e = this.context.getEvent(id);
            if (e != null)
                this.events.add(e);
        }
        this.all = this.events.isEmpty();
        if (this.events.isEmpty())
            this.events.addAll(this.context.availableEvents());
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

}

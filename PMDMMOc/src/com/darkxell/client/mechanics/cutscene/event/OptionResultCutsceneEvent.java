package com.darkxell.client.mechanics.cutscene.event;

import java.util.ArrayList;
import java.util.List;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.model.cutscene.event.OptionResultCutsceneEventModel;

public class OptionResultCutsceneEvent extends CutsceneEvent implements CutsceneContext {

    public final ArrayList<CutsceneEvent> results;
    public final OptionDialogCutsceneEvent target;
    private final OptionResultCutsceneEventModel model;

    public OptionResultCutsceneEvent(OptionResultCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
        this.target = (OptionDialogCutsceneEvent) this.context.getEvent(model.getDialog());
        this.results = new ArrayList<>();
        this.model.getResults().forEach(r -> this.results.add(CutsceneEvent.create(r, context)));
    }

    @Override
    public List<CutsceneEvent> availableEvents() {
        ArrayList<CutsceneEvent> events = new ArrayList<>(this.context.availableEvents());
        for (CutsceneEvent e : this.results)
            if (e != null)
                events.add(e);
        return events;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.target != null && this.target instanceof OptionDialogCutsceneEvent
                && this.target.chosen() == this.model.getOption())
            this.context.parent().player.addEvents(this.results);
    }

    @Override
    public Cutscene parent() {
        return this.context.parent();
    }

    @Override
    public String toString() {
        return this.displayID() + "If choice for event (" + this.target + ") is " + this.model.getOption() + ": Create "
                + this.results.size() + " events";
    }

}

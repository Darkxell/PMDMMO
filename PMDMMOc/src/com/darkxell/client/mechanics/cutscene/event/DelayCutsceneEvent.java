package com.darkxell.client.mechanics.cutscene.event;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.model.cutscene.event.DelayCutsceneEventModel;

public class DelayCutsceneEvent extends CutsceneEvent {

    private final DelayCutsceneEventModel model;
    private int tick = 0;

    public DelayCutsceneEvent(DelayCutsceneEventModel model) {
        super(model);
        this.model = model;
    }

    public DelayCutsceneEvent(DelayCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
    }

    public int getDuration() {
        return this.model.getTicks();
    }

    @Override
    public boolean isOver() {
        return this.tick == this.getDuration();
    }

    @Override
    public String toString() {
        return this.displayID() + "Wait for " + this.getDuration() + " ticks";
    }

    @Override
    public void update() {
        super.update();
        if (!this.isOver())
            ++this.tick;
    }

}

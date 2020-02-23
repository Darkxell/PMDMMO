package com.darkxell.client.mechanics.cutscene.event;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.model.cutscene.event.DrawMapCutsceneEventModel;
import com.darkxell.client.state.dialog.NarratorDialogScreen;

public class DrawMapCutsceneEvent extends CutsceneEvent {

    private final DrawMapCutsceneEventModel model;
    private int tick;

    public DrawMapCutsceneEvent(DrawMapCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
    }

    @Override
    public boolean isOver() {
        return this.tick >= NarratorDialogScreen.FADETIME;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.tick = 0;
        if ((this.shouldDraw() && this.context.parent().player.mapAlpha == 1)
                || (!this.shouldDraw() && this.context.parent().player.mapAlpha == 0))
            this.tick = NarratorDialogScreen.FADETIME;
    }

    public boolean shouldDraw() {
        return this.model.getDrawMap();
    }

    @Override
    public String toString() {
        if (this.shouldDraw())
            return "Start drawing the map";
        return "Stop drawing the map";
    }

    @Override
    public void update() {
        super.update();
        ++this.tick;
        double alpha = this.tick * 1. / NarratorDialogScreen.FADETIME;
        if (!this.shouldDraw())
            alpha = (NarratorDialogScreen.FADETIME - this.tick) * 1. / NarratorDialogScreen.FADETIME;
        this.context.parent().player.mapAlpha = alpha;
    }

}

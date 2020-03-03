package com.darkxell.client.mechanics.cutscene.event;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.CutsceneFunctions;
import com.darkxell.client.model.cutscene.event.FunctionCutsceneEventModel;

public class FunctionCutsceneEvent extends CutsceneEvent {

    private final FunctionCutsceneEventModel model;

    public FunctionCutsceneEvent(FunctionCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
    }

    public String functionID() {
        return this.model.getFunctionID();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.functionID() != null)
            CutsceneFunctions.call(this.functionID(), this.context.parent(), this);
    }

}

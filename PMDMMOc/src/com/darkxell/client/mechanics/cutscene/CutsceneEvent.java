package com.darkxell.client.mechanics.cutscene;

import com.darkxell.client.mechanics.cutscene.event.*;
import com.darkxell.client.model.cutscene.event.*;

public abstract class CutsceneEvent {

    public static CutsceneEvent create(CutsceneEventModel model, CutsceneContext context) {
        // Remember to add to Editor as well (SelectEventTypeController#CutsceneEventType).
        switch (model.type) {
        case animate:
            return new AnimateCutsceneEvent((AnimateCutsceneEventModel) model, context);

        case camera:
            return new CameraCutsceneEvent((CameraCutsceneEventModel) model, context);

        case delay:
            return new DelayCutsceneEvent((DelayCutsceneEventModel) model, context);

        case despawn:
            return new DespawnCutsceneEvent((DespawnCutsceneEventModel) model, context);

        case dialog:
            return new DialogCutsceneEvent((DialogCutsceneEventModel) model, context);

        case drawmap:
            return new DrawMapCutsceneEvent((DrawMapCutsceneEventModel) model, context);

        case function:
            return new FunctionCutsceneEvent((FunctionCutsceneEventModel) model, context);

        case move:
            return new MoveCutsceneEvent((MoveCutsceneEventModel) model, context);

        case music:
            return new MusicCutsceneEvent((MusicCutsceneEventModel) model, context);

        case option:
            return new OptionDialogCutsceneEvent((OptionDialogCutsceneEventModel) model, context);

        case optionresult:
            return new OptionResultCutsceneEvent((OptionResultCutsceneEventModel) model, context);

        case rotate:
            return new RotateCutsceneEvent((RotateCutsceneEventModel) model, context);

        case setstate:
            return new SetStateCutsceneEvent((SetStateCutsceneEventModel) model, context);

        case setanimated:
            return new SetAnimatedCutsceneEvent((SetAnimatedCutsceneEventModel) model, context);

        case sound:
            return new SoundCutsceneEvent((SoundCutsceneEventModel) model, context);

        case spawn:
            return new SpawnCutsceneEvent((SpawnCutsceneEventModel) model, context);

        case wait:
            return new WaitCutsceneEvent((WaitCutsceneEventModel) model, context);

        default:
            return null;
        }
    }

    protected CutsceneContext context;
    private final CutsceneEventModel model;

    public CutsceneEvent(CutsceneEventModel model) {
        this.model = model;
        this.context = null;
    }

    public CutsceneEvent(CutsceneEventModel model, CutsceneContext context) {
        this.model = model;
        this.context = context;
    }

    public int getID() {
        return this.model.getID() == null ? -1 : this.model.getID();
    }

    public boolean isOver() {
        return true;
    }

    public void onFinish() {
    }

    public void onStart() {
    }

    public void update() {
    }

}

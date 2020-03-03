package com.darkxell.client.mechanics.cutscene.end;

import java.util.Random;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.model.cutscene.end.EnterDungeonCutsceneEndModel;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.StateManager;

public class EnterDungeonCutsceneEnd extends CutsceneEnd {

    private final EnterDungeonCutsceneEndModel model;

    public EnterDungeonCutsceneEnd(Cutscene cutscene, EnterDungeonCutsceneEndModel model) {
        super(cutscene, model);
        this.model = model;
    }

    public int getDungeonID() {
        return this.model.getDungeonID();
    }

    @Override
    public void onCutsceneEnd() {
        super.onCutsceneEnd();
        AbstractState fadesOut = null;
        if (this.fadesOut())
            fadesOut = Persistence.cutsceneState;
        if (Persistence.socketendpoint.connectionStatus() == GameSocketEndpoint.CONNECTED)
            StateManager.setDungeonState(fadesOut, this.getDungeonID());
        else
            StateManager.setDungeonState(fadesOut, this.getDungeonID(), new Random().nextLong());
    }

}

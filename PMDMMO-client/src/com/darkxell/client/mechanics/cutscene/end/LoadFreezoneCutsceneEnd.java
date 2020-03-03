package com.darkxell.client.mechanics.cutscene.end;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.model.cutscene.end.LoadFreezoneCutsceneEndModel;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.common.util.Direction;
import com.darkxell.common.zones.FreezoneInfo;

public class LoadFreezoneCutsceneEnd extends CutsceneEnd {

    private final LoadFreezoneCutsceneEndModel model;

    public LoadFreezoneCutsceneEnd(Cutscene cutscene, LoadFreezoneCutsceneEndModel model) {
        super(cutscene, model);
        this.model = model;
    }

    public Direction getDirection() {
        return this.model.getDirection();
    }

    public FreezoneInfo getFreezone() {
        return this.model.getFreezone();
    }

    public int getX() {
        return this.model.getXPos();
    }

    public int getY() {
        return this.model.getYPos();
    }

    @Override
    public void onCutsceneEnd() {
        super.onCutsceneEnd();
        int camX = (int) Persistence.freezoneCamera.x, camY = (int) Persistence.freezoneCamera.y;
        int x = this.getX(), y = this.getY();
        if (this.cutscene.creation.getFreezone() != this.getFreezone()) {
            camX = x - PrincipalMainState.displayWidth / 8 / 2;
            camY = y - PrincipalMainState.displayHeight / 8 / 2;
        } else {
            CutscenePokemon e = this.cutscene.player.getPlayerEntity();
            if (e != null) {
                if (x == -1)
                    x = (int) e.xPos;
                if (y == -1)
                    y = (int) e.yPos;
            }
        }
        StateManager.setExploreState(this.getFreezone(), this.getDirection(), x, y, this.fadesOut());
        Persistence.freezoneCamera.x = camX;
        Persistence.freezoneCamera.y = camY;
    }

}

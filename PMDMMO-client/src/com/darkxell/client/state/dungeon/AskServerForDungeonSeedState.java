package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.PlayerLoadingState;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.ui.Keys.Key;

public class AskServerForDungeonSeedState extends AbstractState {

    public final int dungeonID;
    private int tick;

    public AskServerForDungeonSeedState(int dungeonID) {
        this.dungeonID = dungeonID;
    }

    @Override
    public void onKeyPressed(Key key) {
    }

    @Override
    public void onKeyReleased(Key key) {
    }

    public void onSeedReceived(int dungeon, long seed) {
        Persistence.isCommunicating = false;
        StateManager.setDungeonState(null, this.dungeonID, seed);
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
    }

    @Override
    public void update() {
        if (this.isMain())
            if (this.tick == 0) {
                Persistence.isCommunicating = true;
                Persistence.socketendpoint.requestDungeonSeed(this.dungeonID);
                this.tick = PlayerLoadingState.TIMEOUT;
            } else
                --this.tick;
    }

}

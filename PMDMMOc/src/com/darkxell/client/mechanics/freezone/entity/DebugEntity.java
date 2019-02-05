package com.darkxell.client.mechanics.freezone.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.common.util.Logger;

class DebugEntity extends FreezoneEntity {
    {
        this.interactive = true;
    }

    @Override
    public void onInteract() {
        Logger.d("Triggered debug entity script.");
        // Do your debug shit here. Please remember to not commit any changes.
        // TRIGGERED politeness police!!! --Cubi
        CutsceneManager.playCutscene("skarmory/team", true);
    }

    @Override
    public void print(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fillRect((int) (super.posX * 8) - 7, (int) (super.posY * 8) - 7, 15, 15);
        g.setColor(Color.RED);
        g.drawString("D", (int) (super.posX * 8) - 6, (int) (super.posY * 8) + 5);
    }
}

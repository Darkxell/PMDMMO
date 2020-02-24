package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.TriggerZone;
import com.darkxell.client.state.StateManager;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

public class BaseInsideFreezone extends FreezoneMap {

    public BaseInsideFreezone() {
        super(readModel("/freezones/base_normal.xml"), 23, 13, FreezoneInfo.BASEINSIDE);
        this.freezonebgm = "base.mp3";
        this.triggerzones.add(new TriggerZone(new DoubleRectangle(20, 32, 5, 2)) {
            @Override
            public void onEnter() {
                if (Persistence.player.getData().storyposition == 4) {
                    CutsceneManager.playCutscene("base/magnetialert", true);
                    Persistence.player.setStoryPosition(5);
                } else if (Persistence.player.getData().storyposition == 8) {
                    CutsceneManager.playCutscene("base/squareopening", true);
                    Persistence.player.setStoryPosition(10);
                } else if (Persistence.player.getData().storyposition == 10
                        && Persistence.player.getData().points >= 10) {
                    CutsceneManager.playCutscene("skarmory/team", true);
                } else if (Persistence.player.getData().storyposition == 14
                        && Persistence.player.getData().points >= 20) {
                    CutsceneManager.playCutscene("sinister/meanies1", true);
                } else if (Persistence.player.getData().storyposition == 16
                        && Persistence.player.getData().points >= 30) {
                    CutsceneManager.playCutscene("sinister/meanies2", true);
                } else if (Persistence.player.getData().storyposition == 13) {
                    CutsceneManager.playCutscene("wigglytuff/letsgo", true);
                } else
                    StateManager.setExploreState(FreezoneInfo.BASE, Direction.SOUTH, 35, 28, true);
            }
        });

        this.playerOnly = true;
    }

}

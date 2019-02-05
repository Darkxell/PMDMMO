package com.darkxell.common.mission;

import java.util.ArrayList;

import com.darkxell.common.dungeon.DungeonExploration;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.mission.dungeon.RescueDungeonMission;
import com.darkxell.common.player.Player;
import com.darkxell.common.util.language.Message;

public abstract class DungeonMission {

    public static DungeonMission create(Player player, Mission mission) {
        switch (mission.getMissiontype()) {
        case Mission.TYPE_RESCUEME:
        case Mission.TYPE_RESCUEHIM:
            return new RescueDungeonMission(player, mission);

        default:
            return null;
        }
    }

    private boolean cleared = false;
    public final Mission missionData;
    public final Player owner;

    public DungeonMission(Player owner, Mission missionData) {
        this.owner = owner;
        this.missionData = missionData;
    }

    public void clear() {
        this.cleared = true;
    }

    /** @return A Message to display to a Player that clears this Mission. */
    public abstract Message clearedMessage();

    /**
     * @param  event - An occuring DungeonEvent.
     * @return       <code>true</code> If the input Event clears this Mission.
     */
    public boolean clearsMission(DungeonEvent event) {
        if (event.floor.id == this.missionData.getFloor())
            return this.clearsMissionTF(event);
        return false;
    }

    /**
     * This method is only called when current Floor is the target Floor of this Mission.
     * 
     * @param  event - An occuring DungeonEvent.
     * @return       <code>true</code> If the input Event clears this Mission.
     */
    protected abstract boolean clearsMissionTF(DungeonEvent event);

    public boolean isCleared() {
        return this.cleared;
    }

    /** Called when the Dungeon starts. */
    public void onDungeonStart(DungeonExploration exploration) {
    }

    /** Called on each new Floor, if this Mission isn't cleared yet. */
    public void onFloorStart(Floor floor, ArrayList<DungeonEvent> events) {
        if (floor.id == this.missionData.getFloor())
            this.onTargetFloorStart(floor, events);
    }

    /** Called when the target Floor of this Mission starts, if this Mission isn't cleared yet. */
    protected void onTargetFloorStart(Floor floor, ArrayList<DungeonEvent> events) {
        events.add(new MessageEvent(floor, new Message("mission.target_floor")));
    }

    @Override
    public String toString() {
        return this.missionData.toString();
    }

}

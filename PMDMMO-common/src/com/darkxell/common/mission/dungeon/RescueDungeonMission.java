package com.darkxell.common.mission.dungeon;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.mission.DungeonMission;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.player.Player;

public class RescueDungeonMission extends DungeonMission
{

	public RescueDungeonMission(Player owner, Mission missionData)
	{
		super(owner, missionData);
	}

	@Override
	protected boolean clearsMissionTF(DungeonEvent event)
	{
		return false;
	}

}

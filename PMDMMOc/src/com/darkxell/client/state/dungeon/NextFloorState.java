package com.darkxell.client.state.dungeon;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.TransitionState;
import com.darkxell.client.state.map.DungeonFloorMap;
import com.darkxell.common.dungeon.Dungeon;
import com.darkxell.common.util.language.Message;

public class NextFloorState extends TransitionState
{
	private static Message createMessage(int floor)
	{
		return Persistance.dungeon.dungeon().name().addSuffix(new Message("<br>", false))
				.addSuffix(new Message("stairs.floor." + (Persistance.dungeon.dungeon().direction == Dungeon.UP ? "up" : "down")).addReplacement("<floor>",
						Integer.toString(floor)));
	}

	public NextFloorState(AbstractState previous, int floor)
	{
		super(previous, null, createMessage(floor));
	}

	@Override
	public void onEnd()
	{
		super.onEnd();
		Persistance.eventProcessor().processPending();
	}

	@Override
	public void onTransitionHalf()
	{
		super.onTransitionHalf();
		Persistance.floor = Persistance.dungeon.currentFloor();
		Persistance.displaymap = new DungeonFloorMap();
		this.next = Persistance.dungeonState = new DungeonState();
		Persistance.dungeonState.floorVisibility.onCameraMoved();
		String ost = "dungeon-" + Persistance.floor.data.soundtrack() + ".mp3";
		if (Persistance.floor.data.isBossFloor()) ost = "boss.mp3";
		Persistance.soundmanager.setBackgroundMusic(SoundsHolder.getSong(ost));
	}

}

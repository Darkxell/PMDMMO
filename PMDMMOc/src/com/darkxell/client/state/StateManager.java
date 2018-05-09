package com.darkxell.client.state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.state.dungeon.NextFloorState;
import com.darkxell.client.state.freezone.FreezoneExploreState;
import com.darkxell.client.state.map.DungeonFloorMap;
import com.darkxell.common.dungeon.DungeonRegistry;
import com.darkxell.common.util.Logger;

/**
 * Describes how a statemanager is supposed to work. A statemanager is expected
 * to display A very big portion of the application, like for example the game /
 * the login facilities...<br/>
 * Note that changing the statemanager removes the previous one completely, and
 * should only be done when the user does significants acts that changes the way
 * he is going to interact with the application after, like for exemple logging
 * in.
 */
public abstract class StateManager {

	public abstract void onKeyPressed(KeyEvent e, short key);

	public abstract void onKeyReleased(KeyEvent e, short key);

	public abstract void onKeyTyped(KeyEvent e);

	public abstract void onMouseClick(int x, int y);

	public abstract void onMouseMove(int x, int y);

	public abstract void onMouseRightClick(int x, int y);

	public abstract void render(Graphics2D g, int width, int height);

	public abstract void update();
	
	public void setState(AbstractState state) {
		Logger.e("Tried to call setState() on the wrong state manager!");
	}

	public AbstractState getCurrentState() {
		Logger.e("Tried to call getCurrentState() on the wrong state manager!");
		return null;
	}
	
	
	// State switching methods
	/** @param mapID - ID of a Map. If null or doesn't match a valid ID, this method will not do anything.
	 * @param xPos, yPos - Coordinates of the Player in the map. If any is -1, uses the default coordinates for that map. */
	public static void setExploreState(String mapID, int xPos, int yPos)
	{
		FreezoneMap map = FreezoneMap.loadMap(mapID);
		if (map != null)
		{
			Persistance.currentmap = map;
			Persistance.freezoneCamera.x = Persistance.currentplayer.x = xPos == -1 ? map.defaultX() : xPos;
			Persistance.freezoneCamera.y = Persistance.currentplayer.y = yPos == -1 ? map.defaultY() : yPos;
		}
		Persistance.stateManager.setState(new FreezoneExploreState());
	}

	/** @param fadeOutState - State to fade out of.
	 * @param dungeonID - ID of a Dungeon. If doesn't match a valid ID, this method will not do anything. */
	public static void setDungeonState(AbstractState fadeOutState, int dungeonID)
	{
		Persistance.dungeon = DungeonRegistry.find(dungeonID).newInstance();
		Persistance.eventProcessor = new ClientEventProcessor(Persistance.dungeon);
		Persistance.floor = Persistance.dungeon.currentFloor();
		Persistance.stateManager.setState(new NextFloorState(fadeOutState, 1));
		Persistance.displaymap = new DungeonFloorMap();
		Persistance.eventProcessor.addToPending(Persistance.dungeon.currentFloor().onFloorStart());
	}
	
}

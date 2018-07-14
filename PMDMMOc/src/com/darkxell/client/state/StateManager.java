package com.darkxell.client.state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.Freezones;
import com.darkxell.client.state.dungeon.DungeonEndState;
import com.darkxell.client.state.dungeon.NextFloorState;
import com.darkxell.client.state.freezone.FreezoneExploreState;
import com.darkxell.client.state.map.LocalMap;
import com.darkxell.common.dungeon.DungeonOutcome;
import com.darkxell.common.dungeon.DungeonRegistry;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;
import com.darkxell.common.zones.FreezoneInfo;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.PrettyPrint;

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

	/**
	 * @param freezone
	 *            - ID of a Map. If null or doesn't match a valid ID, this
	 *            method will not do anything.
	 * @param xPos,
	 *            yPos - Coordinates of the Player in the map. If any is -1,
	 *            uses the default coordinates for that map.
	 * @param direction
	 * 			  - The direction to face when entering the Freezone. null to keep current direction.
	 */
	public static void setExploreState(FreezoneInfo freezone, Direction direction, int xPos, int yPos) {
		FreezoneMap map = Freezones.loadMap(freezone);
		if (map == null)
			return;
		setExploreState(map, direction, xPos, yPos);
	}

	/**
	 * @param map
	 *            - Map to explore. If null, this method will not do anything.
	 * @param xPos,
	 *            yPos - Coordinates of the Player in the map. If any is -1,
	 *            uses the default coordinates for that map.
	 */
	public static void setExploreState(FreezoneMap map, Direction direction, int xPos, int yPos) {
		AbstractState next;
		if (Persistance.stateManager.getCurrentState() instanceof FreezoneExploreState)
			next = Persistance.stateManager.getCurrentState();
		else
			next = new FreezoneExploreState();
		Persistance.stateManager.setState(new TransitionState(Persistance.stateManager.getCurrentState(), next) {
			@Override
			public void onTransitionHalf() {
				super.onTransitionHalf();
				((FreezoneExploreState) next).musicset = false;
				Persistance.currentmap = map;
				Persistance.freezoneCamera.x = Persistance.currentplayer.x = xPos == -1 ? map.defaultX() : xPos;
				Persistance.freezoneCamera.y = Persistance.currentplayer.y = yPos == -1 ? map.defaultY() : yPos;
				Persistance.displaymap = LocalMap.instance;
				if (direction != null) Persistance.currentplayer.renderer().sprite().setFacingDirection(direction);
			}
		});
	}

	/**
	 * @param fadeOutState
	 *            - State to fade out of.
	 * @param dungeonID
	 *            - ID of a Dungeon. If doesn't match a valid ID, this method
	 *            will not do anything.
	 * @param seed
	 *            - Seed to use for RNG in the Dungeon.
	 */
	public static void setDungeonState(AbstractState fadeOutState, int dungeonID, long seed) {
		Persistance.dungeon = DungeonRegistry.find(dungeonID).newInstance(seed);
		Persistance.dungeon.eventProcessor = new ClientEventProcessor(Persistance.dungeon);
		Persistance.dungeon.addPlayer(Persistance.player);
		Persistance.floor = Persistance.dungeon.initiateExploration();
		Persistance.stateManager.setState(new NextFloorState(fadeOutState, 1));
	}

	public static void onDungeonEnd(DungeonOutcome outcome) {
		if (Persistance.isUnitTesting)
			Launcher.stopGame();
		if (Persistance.saveDungeonExplorations) {
			JsonObject o = Persistance.dungeon.communication.explorationSummary(true);
			try {
				BufferedWriter fw = new BufferedWriter(new FileWriter(
						new File("dungeon-" + Persistance.dungeon.id + "-" + Persistance.dungeon.seed + ".json")));
				fw.write(o.toString(PrettyPrint.indentWithTabs()));
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		DungeonEndState state = new DungeonEndState(outcome);

		Persistance.stateManager.setState(new TransitionState(Persistance.dungeonState, state) {
			@Override
			public void onTransitionHalf() {
				super.onTransitionHalf();
				Persistance.displaymap = null;
			}
		});
	}

}

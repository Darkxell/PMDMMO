package com.darkxell.client.launchable;

import com.darkxell.client.mechanics.chat.ChatBox;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.FreezonePlayer;
import com.darkxell.client.mechanics.freezones.entities.FreezoneCamera;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.dialog.ComplexDialog;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.map.AbstractDisplayMap;
import com.darkxell.client.state.map.LocalMap;
import com.darkxell.client.ui.Frame;
import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.player.Player;

/**
 * This class contains various static references to objects being used very
 * often.
 */
public abstract class Persistance {

	// APPLICATION RELATED OBJECTS
	public static Frame frame;
	public static StateManager stateManager;
	public static SoundManager soundmanager;
	public static ChatBox chatbox = new ChatBox();
	public static AbstractDisplayMap displaymap = LocalMap.instance;
	public static GameSocketEndpoint socketendpoint = new GameSocketEndpoint();

	public static boolean isUnitTesting = false;
	public static boolean isCommunicating = false;

	// FREEZONE RELATED OBJECTS
	public static FreezoneMap currentmap;
	public static FreezonePlayer currentplayer = new FreezonePlayer(
			new PokemonSprite(PokemonSpritesets.getSpriteset(0)), 35, 28);
	public static FreezoneCamera freezoneCamera = new FreezoneCamera(currentplayer);

	public static AbstractState cutsceneState = null;
	public static ComplexDialog currentDialog = null;

	// DUNGEON RELATED OBJECTS
	public static DungeonInstance dungeon;
	public static DungeonState dungeonState;
	public static MasterDungeonRenderer dungeonRenderer;
	public static Floor floor;
	public static Player player;

	public static ClientEventProcessor eventProcessor() {
		return dungeon == null ? null : (ClientEventProcessor) dungeon.eventProcessor;
	}

	/**
	 * Displays the debug information. Careful, this is not optimized and will
	 * have a high CPU drain. It also makes the game really ugly, it's a debug
	 * mode...
	 */
	public static boolean debugdisplaymode = false;
	/** Displays all input/outputs of the gamesocket to the console. */
	public static boolean debugwiresharkmode = false;

	/** If true, data for pokemon, dungeon, moves, etc. is saved on exit. */
	public static boolean saveDataOnExit = false;

	/** If true, dungeon explorations will be saved as Json. */
	public static final boolean saveDungeonExplorations = false;

}

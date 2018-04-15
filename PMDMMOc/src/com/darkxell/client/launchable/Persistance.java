package com.darkxell.client.launchable;

import java.util.Random;

import com.darkxell.client.mechanics.chat.ChatBox;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.FreezonePlayer;
import com.darkxell.client.mechanics.freezones.entities.FreezoneCamera;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.map.AbstractDisplayMap;
import com.darkxell.client.state.map.LocalMap;
import com.darkxell.client.ui.Frame;
import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.item.ItemID;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.PokemonRegistry;

/** This class contains various static references to objects being used very often. */
public abstract class Persistance
{

	// APPLICATION RELATED OBJECTS
	public static Frame frame;
	public static StateManager stateManager;
	public static SoundManager soundmanager;
	public static ChatBox chatbox = new ChatBox();
	public static AbstractDisplayMap displaymap = LocalMap.instance;
	public static GameSocketEndpoint socketendpoint = new GameSocketEndpoint();

	// FREEZONE RELATED OBJECTS
	public static FreezoneMap currentmap;
	public static FreezonePlayer currentplayer = new FreezonePlayer(new PokemonSprite(PokemonSpritesets.getSpriteset(0)), 35, 28);
	public static FreezoneCamera playerCamera = new FreezoneCamera(currentplayer);

	// DUNGEON RELATED OBJECTS
	public static DungeonInstance dungeon;
	public static DungeonState dungeonState;
	public static MasterDungeonRenderer dungeonRenderer;
	public static Floor floor;
	public static Player player;
	public static ClientEventProcessor eventProcessor;

	/** Displays the debug information. Careful, this is not optimized and will have a high CPU drain. It also makes the game really ugly, it's a debug mode... */
	public static boolean debugdisplaymode = false;

	/** If true, data for pokemon, dungeon, moves, etc. is saved on exit. */
	public static boolean saveDataOnExit = false;

	public static void setPlayer(Player newPlayer)
	{
		player = newPlayer;
		currentplayer.setPlayer(player);
	}

}

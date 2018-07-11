package com.darkxell.client.discord;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.freezone.FreezoneExploreState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.map.LocalMap;
import com.darkxell.common.util.Logger;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

/**
 * Discord handler made especially for PMDMMO. THis class specifies all the
 * handlers and behaviours that can be implemented.
 */
public class DiscordEventHandlerForPMDMMO {

	/** Creates a new instance of discordEventHandler for PMDMMO. */
	public static DiscordEventHandlers createHandler() {
		return new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
			Logger.i("Connected to discord : " + user.username + "#" + user.discriminator);
		}).build();
	}

	private static final int updatecooldown = 120;
	private static int actualcooldown = updatecooldown;

	private static String smallimage = "badgeicon_6";
	private static String smallimagetext = "Platinium team badge";

	/**
	 * Handles the discord rich presence api. This method is on update after
	 * updating the game.
	 */
	public static void handleDiscordRP() {
		// Handles the discordRP cooldown
		actualcooldown--;
		if (actualcooldown >= 1)
			return;
		actualcooldown = updatecooldown;
		// Updates discord if cooldown is done
		if (Persistance.stateManager instanceof PrincipalMainState) {
			PrincipalMainState pmst = (PrincipalMainState) Persistance.stateManager;
			if (pmst.getCurrentState() instanceof FreezoneExploreState) {
				DiscordRichPresence.Builder rich = new DiscordRichPresence.Builder("Hanging around")
						.setBigImage("squareicon", "").setSmallImage(smallimage, smallimagetext);
				if (Persistance.displaymap instanceof LocalMap)
					rich.setDetails(((LocalMap) Persistance.displaymap).currentlocation.displayname.toString());
				DiscordRPC.discordUpdatePresence(rich.build());
			} else if (pmst.getCurrentState() instanceof DungeonState) {
				DiscordRichPresence.Builder rich = new DiscordRichPresence.Builder("Exploring a dungeon")
						.setBigImage("mapicon", "").setSmallImage(smallimage, smallimagetext);
				rich.setDetails(Persistance.dungeon.dungeon().name().toString());
				DiscordRPC.discordUpdatePresence(rich.build());
			}

		}
	}

}

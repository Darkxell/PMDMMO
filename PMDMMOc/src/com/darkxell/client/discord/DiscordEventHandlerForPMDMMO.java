package com.darkxell.client.discord;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.freezone.FreezoneExploreState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.common.util.Logger;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

/**
 * Discord handler made especially for PMDMMO. THis class specifies all the
 * handlers and behaviours that can be implemented.
 */
public class DiscordEventHandlerForPMDMMO {

	/**
	 * Creates a new DiscordHandler. This handler will have it's own thread that
	 * will call the handleDiscordRP() method on cooldown. Using this object is
	 * highly recommended as the discordRPC lib seems to be unstable and should
	 * be ran in it's own fail safe thread.
	 */
	public DiscordEventHandlerForPMDMMO() {
		this.runner = new Thread(new Runnable() {

			@Override
			public void run() {
				// Sets the default discord presence for the game
				DiscordRPC.discordInitialize("463408543572426762", createHandler(), true);
				DiscordRichPresence rich = new DiscordRichPresence.Builder("In logging screen")
						.setBigImage("main_big", "").build();
				DiscordRPC.discordUpdatePresence(rich);
				while (isrunning && Launcher.isRunning) {
					// As long as the RP thread and the game are running, update
					// discord.
					try {
						Thread.sleep(DRPCOOLDOWN);
					} catch (InterruptedException e) {
					}
					handleDiscordRP();
				}
				// Stops the discord API if the game is stopping.
				if (!Launcher.isRunning)
					try {
						DiscordRPC.discordShutdown();
					} catch (Exception e) {
						Logger.e("Could not shutdown discord RPC.");
					}
			}
		});
		this.runner.setName("Discord rich presence thread");
	}

	/** The discord rp update cooldown, in miliseconds. */
	private static final int DRPCOOLDOWN = 5000;

	private Thread runner;
	private boolean isrunning;

	/** Starts the updating thread. */
	public void start() {
		isrunning = true;
		runner.start();
	}

	/**
	 * Stops the updating thread. This method is fail safe and will never throw
	 * an exception even if the thread is already stopped or crashed. That's the
	 * point.
	 */
	public void stop() {
		isrunning = false;
	}

	/*---------------Object mechanics below--------------------*/

	/** Creates a new instance of discordEventHandler for PMDMMO. */
	private DiscordEventHandlers createHandler() {
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
	private void handleDiscordRP() {
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
				rich.setDetails(Persistance.currentmap.info.getName().toString());
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

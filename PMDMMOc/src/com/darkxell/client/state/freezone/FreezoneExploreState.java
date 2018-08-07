package com.darkxell.client.state.freezone;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.zones.BaseFreezone;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.client.state.menu.freezone.FreezoneMenuState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.JsonObject;

public class FreezoneExploreState extends AbstractFreezoneState
{

	private int serversynccooldown = 0;

	public FreezoneExploreState()
	{}

	@Override
	public void onEnd()
	{
		super.onEnd();
		Persistance.currentplayer.forceStop();
	}

	@Override
	public void onKeyPressed(Key key)
	{
		if (key == Key.MENU)
		{
			Persistance.currentplayer.forceStop();
			Persistance.stateManager.setState(new FreezoneMenuState(this));
		} else if (Persistance.currentmap != null) Persistance.currentplayer.pressKey(key);
	}

	@Override
	public void onKeyReleased(Key key)
	{
		if (Persistance.currentmap != null) Persistance.currentplayer.releaseKey(key);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if (Key.UP.isPressed()) Persistance.currentplayer.pressKey(Key.UP);
		if (Key.RIGHT.isPressed()) Persistance.currentplayer.pressKey(Key.RIGHT);
		if (Key.DOWN.isPressed()) Persistance.currentplayer.pressKey(Key.DOWN);
		if (Key.LEFT.isPressed()) Persistance.currentplayer.pressKey(Key.LEFT);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		FreezoneMap map = Persistance.currentmap;
		if (map != null)
		{
			if (this.isMain() && Persistance.currentplayer.canInteract())
			{
				g.drawImage(Sprites.button.image(), width - 70, 5, null);
				TextRenderer.render(g, new Message("ui.interact"), width - 50, 10);
			}

			if (Persistance.debugdisplaymode)
			{
				g.setColor(Color.BLACK);
				TextRenderer.render(g, "UPS: " + Launcher.getUps() + ", FPS: " + Launcher.getFps(), 1, TextRenderer.height());
				TextRenderer.render(g, "Position: " + Persistance.currentplayer.x + " / " + Persistance.currentplayer.y, 1, TextRenderer.height() * 3);
			}
		}
	}

	@Override
	public void update()
	{
		if (Persistance.currentmap == null) Persistance.currentmap = new BaseFreezone();
		super.update();
		// Sends the serverync message if it's time to do so
		if (serversynccooldown > 15 && serversynccooldown != -1) if (Persistance.socketendpoint.connectionStatus() != GameSocketEndpoint.CONNECTED)
		{
			serversynccooldown = -1;
			Logger.w("Game socket endpoint is not connected, server sync has been deactivated for this FreezoneExploreState.");
		} else
		{
			serversynccooldown = 0;
			try
			{
				String message = "";
				JsonObject mess = new JsonObject().add("action", "freezoneposition").add("posfx", Persistance.currentplayer.x)
						.add("posfy", Persistance.currentplayer.y).add("currentpokemon", Persistance.currentplayer.renderer().sprite().pointer.pokemonID + "")
						.add("freezoneid", Persistance.currentmap.getInfo().id);
				message = mess.toString();
				Persistance.socketendpoint.sendMessage(message);
			} catch (Exception e)
			{
				Logger.w("Could not send freezone information message to the server.");
			}
		}
		else if (serversynccooldown != -1) ++serversynccooldown;

		if (this.isMain()) for (int i = 0; i < Persistance.currentmap.triggerzones.size(); i++)
			if (Persistance.currentmap.triggerzones.get(i).hitbox
					.intersects(Persistance.currentplayer.getHitboxAt(Persistance.currentplayer.x, Persistance.currentplayer.y)))
			{
				Persistance.currentmap.triggerzones.get(i).onEnter();
				break;
			}

	}

}

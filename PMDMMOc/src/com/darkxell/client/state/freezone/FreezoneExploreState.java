package com.darkxell.client.state.freezone;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistence;
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
		Persistence.currentplayer.forceStop();
	}

	@Override
	public void onKeyPressed(Key key)
	{
		if (key == Key.MENU)
		{
			Persistence.currentplayer.forceStop();
			Persistence.stateManager.setState(new FreezoneMenuState(this));
		} else if (Persistence.currentmap != null) Persistence.currentplayer.pressKey(key);
	}

	@Override
	public void onKeyReleased(Key key)
	{
		if (Persistence.currentmap != null) Persistence.currentplayer.releaseKey(key);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if (Key.UP.isPressed()) Persistence.currentplayer.pressKey(Key.UP);
		if (Key.RIGHT.isPressed()) Persistence.currentplayer.pressKey(Key.RIGHT);
		if (Key.DOWN.isPressed()) Persistence.currentplayer.pressKey(Key.DOWN);
		if (Key.LEFT.isPressed()) Persistence.currentplayer.pressKey(Key.LEFT);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		FreezoneMap map = Persistence.currentmap;
		if (map != null)
		{
			if (this.isMain() && Persistence.currentplayer.canInteract())
			{
				g.drawImage(Sprites.Res_Hud.button.image(), width - 70, 5, null);
				TextRenderer.render(g, new Message("ui.interact"), width - 50, 10);
			}

			if (Persistence.debugdisplaymode)
			{
				g.setColor(Color.BLACK);
				TextRenderer.render(g, "UPS: " + Launcher.getUps() + ", FPS: " + Launcher.getFps(), 1, TextRenderer.height());
				TextRenderer.render(g, "Position: " + Persistence.currentplayer.x + " / " + Persistence.currentplayer.y, 1, TextRenderer.height() * 3);
			}
		}
	}

	@Override
	public void update()
	{
		if (Persistence.currentmap == null) Persistence.currentmap = new BaseFreezone();
		super.update();
		// Sends the serverync message if it's time to do so
		if (serversynccooldown > 15 && serversynccooldown != -1) if (Persistence.socketendpoint.connectionStatus() != GameSocketEndpoint.CONNECTED)
		{
			serversynccooldown = -1;
			Logger.w("Game socket endpoint is not connected, server sync has been deactivated for this FreezoneExploreState.");
		} else
		{
			serversynccooldown = 0;
			try
			{
				String message = "";
				JsonObject mess = new JsonObject().add("action", "freezoneposition").add("posfx", Persistence.currentplayer.x)
						.add("posfy", Persistence.currentplayer.y).add("currentpokemon", Persistence.currentplayer.renderer().sprite().pointer.data.id + "")
						.add("freezoneid", Persistence.currentmap.getInfo().id);
				message = mess.toString();
				Persistence.socketendpoint.sendMessage(message);
			} catch (Exception e)
			{
				Logger.w("Could not send freezone information message to the server.");
			}
		}
		else if (serversynccooldown != -1) ++serversynccooldown;

		if (this.isMain()) for (int i = 0; i < Persistence.currentmap.triggerzones.size(); i++)
			if (Persistence.currentmap.triggerzones.get(i).hitbox
					.intersects(Persistence.currentplayer.getHitboxAt(Persistence.currentplayer.x, Persistence.currentplayer.y)))
			{
				Persistence.currentmap.triggerzones.get(i).onEnter();
				break;
			}

	}

}

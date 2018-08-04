package com.darkxell.client.state.menu.freezone;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Predicate;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.resources.images.others.Hud;
import com.darkxell.client.resources.images.others.MapResources;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.freezone.FreezoneExploreState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.DungeonRegistry;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class DungeonSelectionMapState extends AbstractState
{

	private int camerax = 0;
	private int cameray = 0;
	private int cursor = 0;
	private ArrayList<Dungeon> dungeonslist;

	public DungeonSelectionMapState()
	{
		super();
		ArrayList<Dungeon> dungeons = new ArrayList<Dungeon>();
		dungeons.addAll(DungeonRegistry.list());
		dungeons.sort(Comparator.naturalOrder());
		dungeons.removeIf(new Predicate<Dungeon>() {

			@Override
			public boolean test(Dungeon d)
			{
				return d.mapx == 0 || d.mapy == 0;
			}
		});
		this.dungeonslist = dungeons;
		{
			if (!this.dungeonslist.isEmpty()) this.camerax = this.dungeonslist.get(0).mapx;
			this.cameray = this.dungeonslist.get(0).mapy;
		}
	}

	public void onDungeonStart(int dungeon, long seed)
	{
		Persistance.isCommunicating = false;
		if (dungeon != this.dungeonslist.get(this.cursor).id) Logger.w("Received dungeon ID to start is different than selected; starting cancelled");
		else StateManager.setDungeonState(this, dungeon, seed);
	}

	@Override
	public void onKeyPressed(Key key)
	{
		switch (key)
		{
			case RIGHT:
				if (cursor >= dungeonslist.size() - 1) cursor = 0;
				else++cursor;
				break;
			case LEFT:
				if (cursor <= 0) cursor = dungeonslist.size() - 1;
				else--cursor;
				break;
			case UP:
				if (cursor < dungeonslist.size() - 10) cursor += 10;
				else cursor = 0;
				break;
			case DOWN:
				if (cursor >= 10) cursor -= 10;
				else cursor = (dungeonslist.size() - 1);
				break;
			case RUN:
				Persistance.isCommunicating = false;
				Persistance.currentplayer.y -= 1;
				Persistance.currentplayer.renderer().sprite().setFacingDirection(Direction.NORTH);
				Persistance.stateManager.setState(new FreezoneExploreState());
				break;
			case ATTACK:
				// Sending dungeonstart to server
				int dungeon = this.dungeonslist.get(this.cursor).id;
				if (Persistance.socketendpoint.connectionStatus() == GameSocketEndpoint.CONNECTED)
				{
					JsonObject root = Json.object();
					root.add("action", "dungeonstart");
					root.add("dungeon", dungeon);
					Persistance.isCommunicating = true;
					Persistance.socketendpoint.sendMessage(root.toString());
				} else this.onDungeonStart(dungeon, new Random().nextLong());
				break;
			default:
				break;
		}
	}

	@Override
	public void onKeyReleased(Key key)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		// CALCULATES TRANSLATIONS
		int translateX = (int) (-camerax + (width / 2));
		int translateY = (int) (-cameray + (height / 2));

		g.translate(translateX, translateY);
		// DRAWS THE MAP
		g.drawImage(MapResources.GLOBALMAP, 0, 0, null);
		for (int i = 0; i < dungeonslist.size(); ++i)
			g.drawImage(i == cursor ? MapResources.PIN_BLUE : MapResources.PIN_YELLOW, dungeonslist.get(i).mapx, dungeonslist.get(i).mapy, null);
		// TRANSLATES THE GRAPHICS BACK
		g.translate(-translateX, -translateY);
		// DRAWS THE HUD OVER THE MAP
		int textxpos = width - Math.max(TextRenderer.width(new Message("dungeonmap.select")), TextRenderer.width(new Message("dungeonmap.return"))) - 10;
		TextRenderer.render(g, new Message("dungeonmap.select"), textxpos, 20);
		TextRenderer.render(g, new Message("dungeonmap.return"), textxpos, 40);

		int temp_width = width - 40;
		int temp_height = temp_width * Hud.textwindow.getHeight() / Hud.textwindow.getWidth();
		Rectangle box = new Rectangle(20, height - temp_height - 20, temp_width, temp_height);
		g.drawImage(Hud.textwindow, box.x, box.y, box.width, box.height, null);
		g.drawImage(MenuHudSpriteset.TAB_ARROW_LEFT, box.x + box.width - 45, box.y, null);
		g.drawImage(MenuHudSpriteset.TAB_ARROW_RIGHT, box.x + box.width - 30, box.y, null);
		String dungeonsmarker = (this.cursor + 1) + " / " + this.dungeonslist.size();
		TextRenderer.render(g, dungeonsmarker, box.x + box.width - 55, box.y + 10);

		TextRenderer.render(g, dungeonslist.get(cursor).name(), box.x + 15, box.y + 10);
	}

	@Override
	public void update()
	{
		if (camerax < dungeonslist.get(cursor).mapx - 6) camerax += 5;
		else if (camerax > dungeonslist.get(cursor).mapx + 6) camerax -= 5;
		if (cameray < dungeonslist.get(cursor).mapy - 6) cameray += 5;
		else if (cameray > dungeonslist.get(cursor).mapy + 6) cameray -= 5;
	}

}

package com.darkxell.client.state.menu.freezone;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.resources.images.others.Hud;
import com.darkxell.client.resources.images.others.MapResources;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.FreezoneExploreState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.map.DungeonFloorMap;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.dungeon.Dungeon;
import com.darkxell.common.dungeon.DungeonRegistry;
import com.darkxell.common.util.language.Message;

public class DungeonSelectionMapState extends AbstractState {

	private ArrayList<Dungeon> dungeonslist;
	private int cursor = 0;
	private int camerax = 0;
	private int cameray = 0;

	public DungeonSelectionMapState() {
		super();
		ArrayList<Dungeon> dungeons = new ArrayList<Dungeon>();
		dungeons.addAll(DungeonRegistry.list());
		dungeons.sort(Comparator.naturalOrder());
		dungeons.removeIf(new Predicate<Dungeon>() {

			@Override
			public boolean test(Dungeon d) {
				return d.mapx == 0 || d.mapy == 0;
			}
		});
		this.dungeonslist = dungeons;
	}

	@Override
	public void onKeyPressed(short key) {
		switch (key) {
		case Keys.KEY_RIGHT:
			if (cursor >= dungeonslist.size() - 1)
				cursor = 0;
			else
				++cursor;
			break;
		case Keys.KEY_LEFT:
			if (cursor <= 0)
				cursor = dungeonslist.size() - 1;
			else
				--cursor;
			break;
		case Keys.KEY_UP:
			if (cursor < dungeonslist.size() - 10)
				cursor += 10;
			else
				cursor = 0;
			break;
		case Keys.KEY_DOWN:
			if (cursor >= 10)
				cursor -= 10;
			else
				cursor = (dungeonslist.size() - 1);
			break;
		case Keys.KEY_RUN:
			Persistance.stateManager.setState(new FreezoneExploreState());
			break;
		case Keys.KEY_ATTACK:
			Persistance.dungeon = dungeonslist.get(cursor).newInstance();
			Persistance.eventProcessor = new ClientEventProcessor(Persistance.dungeon);
			Persistance.floor = Persistance.dungeon.currentFloor();
			Persistance.floor.generate();
			Persistance.stateManager.setState(Persistance.dungeonState = new DungeonState());
			Persistance.displaymap = new DungeonFloorMap();
			Persistance.eventProcessor.processEvents(Persistance.dungeon.currentFloor().onFloorStart());
			break;
		default:
			break;
		}
	}

	@Override
	public void onKeyReleased(short key) {
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		// CALCULATES TRANSLATIONS
		int translateX = (int) (-camerax + (width / 2));
		int translateY = (int) (-cameray + (height / 2));

		g.translate(translateX, translateY);
		// DRAWS THE MAP
		g.drawImage(MapResources.GLOBALMAP, 0, 0, null);
		for (int i = 0; i < dungeonslist.size(); ++i)
			g.drawImage(i == cursor ? MapResources.PIN_BLUE : MapResources.PIN_YELLOW, dungeonslist.get(i).mapx,
					dungeonslist.get(i).mapy, null);
		// TRANSLATES THE GRAPHICS BACK
		g.translate(-translateX, -translateY);
		// DRAWS THE HUD OVER THE MAP
		int textxpos = width - Math.max(TextRenderer.instance.width(new Message("dungeonmap.select")),
				TextRenderer.instance.width(new Message("dungeonmap.return"))) - 10;
		TextRenderer.instance.render(g, new Message("dungeonmap.select"), textxpos, 20);
		TextRenderer.instance.render(g, new Message("dungeonmap.return"), textxpos, 40);

		int temp_width = width - 40;
		int temp_height = temp_width * Hud.textwindow.getHeight() / Hud.textwindow.getWidth();
		Rectangle box = new Rectangle(20, height - temp_height - 20, temp_width, temp_height);
		g.drawImage(Hud.textwindow, box.x, box.y, box.width, box.height, null);
		g.drawImage(MenuHudSpriteset.TAB_ARROW_LEFT, box.x + box.width - 45, box.y, null);
		g.drawImage(MenuHudSpriteset.TAB_ARROW_RIGHT, box.x + box.width - 30, box.y, null);
		String dungeonsmarker = (this.cursor + 1) + " / " + this.dungeonslist.size();
		TextRenderer.instance.render(g, dungeonsmarker, box.x + box.width - 55, box.y + 10);

		TextRenderer.instance.render(g, dungeonslist.get(cursor).name(), box.x + 15, box.y + 10);
	}

	@Override
	public void update() {
		if (camerax < dungeonslist.get(cursor).mapx - 6)
			camerax += 5;
		else if (camerax > dungeonslist.get(cursor).mapx + 6)
			camerax -= 5;
		if (cameray < dungeonslist.get(cursor).mapy - 6)
			cameray += 5;
		else if (cameray > dungeonslist.get(cursor).mapy + 6)
			cameray -= 5;
	}

}

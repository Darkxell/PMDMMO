package com.darkxell.client.state.menu.freezone;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.resources.images.MapResources;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.FreezoneExploreState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.map.DungeonFloorMap;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.dungeon.Dungeon;
import com.darkxell.common.dungeon.DungeonRegistry;

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
